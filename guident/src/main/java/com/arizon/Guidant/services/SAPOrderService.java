package com.arizon.Guidant.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.model.sap.PricingElement;
import com.arizon.Guidant.model.sap.PricingElementWrapper;
import com.arizon.Guidant.model.sap.SalesOrder;
import com.arizon.Guidant.model.sap.SalesOrderItem;
import com.arizon.Guidant.model.sap.SalesOrderItemWrapper;
import com.arizon.Guidant.model.sap.SalesOrderPartner;
import com.arizon.Guidant.model.sap.SalesOrderPartnerWrapper;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.Guidant.util.SAPDateUtils;
import com.arizon.customercommon.model.BCCustomerAttributeDTO;
import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.entity.OCBCOrderProductTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;
import com.arizon.ordercommons.entity.OCBCShippingAddressTransaction;
import com.arizon.ordercommons.model.ShippingAddressesDTO;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SAPOrderService {

	
	  @Autowired
	  OCBCOrderService ocbcOrderService;
	  
	  @Autowired
	  OrderSourceCustomerService orderSourceCustomerService;
	  
	    
		public String transformBCOrder(OCBCOrderTableTransaction bcOrder,List<OCBCOrderProductTableTransaction> bcOrderproducts, OCBCShippingAddressTransaction shippingAddressTransaction) {
			String rtnstatus = null;
			try {
		  	    log.info("Transforming BigCommerce order id : "+ bcOrder.getSourceOrderId());
				SalesOrder salesOrder = new SalesOrder();
				setSalesOrderHeader(salesOrder, bcOrder , shippingAddressTransaction);
				setSalesOrderItem(salesOrder, bcOrder, bcOrderproducts);
				ObjectMapper objectMapper = new ObjectMapper();
				String salesOrderJson = objectMapper.writeValueAsString(salesOrder);
				log.info("Sales Order JSON: " + salesOrderJson);
				return salesOrderJson;
			} catch (Exception e) {
				log.error("Error transforming BigCommerce order id : "+ bcOrder.getSourceOrderId()+ GuidantUtil.getStackTrace(e));
				return  OCConstants.failed;	
			}
			
		}
	     
	    public void setSalesOrderHeader(SalesOrder salesOrder , OCBCOrderTableTransaction bcOrder, OCBCShippingAddressTransaction shippingAddressTransaction) {
	    	log.info("Setting Sales Order Header for order id : " + bcOrder.getSourceOrderId());
	    	salesOrder.setTransactionCurrency(bcOrder.getCurrency());
	    	salesOrder.setDistributionChannel(secrects.getDistributionChannel());
	    	salesOrder.setSalesOrderType(secrects.getSalesOrderType());
	    	salesOrder.setTotalNetAmount(GuidantConstants.AMOUNTZERO);
	    	salesOrder.setOrganizationDivision(secrects.getOrganizationDivision());
	    	salesOrder.setSalesOrderTypeInternalCode(secrects.getSalesOrderTypeInternalCode());
	    	//salesOrder.setSalesOrganization("1720"); 
	    	salesOrder.setPurchaseOrderByCustomer(bcOrder.getSourceOrderId().toString());
	    	salesOrder.setAccountingDocExternalReference(secrects.getAccountingDocExternalReference());
	    	salesOrder.setPriceDetnExchangeRate(secrects.getPriceDetnExchangeRate());
	    	salesOrder.setCreationDate(SAPDateUtils.convertBCDateFormatToSAP(bcOrder.getOrderCreatedDate()));

	    	List<BCCustomerAttributeDTO> customerAttributes = orderSourceCustomerService.getBCCustomerAttributeByCutomerId(bcOrder.sourceCustomerId);
	    	salesOrder.setSoldToParty(orderSourceCustomerService.getCustomerAttributeValue(customerAttributes,secrects.getSoldToPartyBCAttributeId()));
	    	salesOrder.setSalesOrganization(orderSourceCustomerService.getCustomerAttributeValue(customerAttributes,secrects.getSalesOrganizationBCAttributeId()));
	    	String shippingAddressShipTo = shippingAddressTransaction.getShippingAddressCode();
	    	String customerShipTo = orderSourceCustomerService.getCustomerAttributeValue(customerAttributes,secrects.getShipToPartyBCAttributeId());
	    	setShipToPartyID(salesOrder, customerShipTo ,shippingAddressShipTo);
	    	
	    }
	    
	    public void setSalesOrderItem(SalesOrder salesOrder, OCBCOrderTableTransaction bcOrder, List<OCBCOrderProductTableTransaction> bcOrderproducts) {
	        List<SalesOrderItem> salesOrderItems = new ArrayList<SalesOrderItem>();
	        Integer incrementalItemId = 10;
	        log.info("Setting Sales Order Items for order id : " + bcOrder.getSourceOrderId());
	        for (OCBCOrderProductTableTransaction bcOrderProduct : bcOrderproducts) {
	        	log.info("Processing product SKU: " + bcOrderProduct.getSku() + " with quantity: " + bcOrderProduct.getQuantity());
	            SalesOrderItem salesOrderItem = new SalesOrderItem();
	            salesOrderItem.setMaterial(bcOrderProduct.getSku());
	            salesOrderItem.setRequestedQuantity(bcOrderProduct.getQuantity().toString());
	            salesOrderItem.setNetAmount(bcOrderProduct.getBasePrice().toString());
	            salesOrderItem.setSalesOrderItem(incrementalItemId.toString());  
	            salesOrderItem.setPricingElementWrapper(setToPricingElement(salesOrderItem, bcOrderProduct, bcOrder));
	            salesOrderItems.add(salesOrderItem);
	            incrementalItemId += 10;
	        }
	        SalesOrderItemWrapper salesOrderItemWrapper = new SalesOrderItemWrapper();
	        salesOrderItemWrapper.setResults(salesOrderItems);
	        salesOrder.setToItem(salesOrderItemWrapper);
	    }
	    
	    public PricingElementWrapper setToPricingElement(SalesOrderItem salesOrderItem, OCBCOrderProductTableTransaction bcOrderProduct , OCBCOrderTableTransaction bcOrder) {
	    	log.info("Setting Pricing Elements for Sales Order Item with SKU: " + salesOrderItem.getMaterial());
	    	List<PricingElement> results = new ArrayList<>();
	    	results.add(setProductPricing(salesOrderItem, bcOrderProduct, bcOrder));
	    	results.add(setProductDiscount(salesOrderItem, bcOrderProduct, bcOrder));
	    	PricingElementWrapper pricingElementWrapper = new PricingElementWrapper();
	    	pricingElementWrapper.setResults(results);
	    	return pricingElementWrapper;
	    }
	    
	    public PricingElement setProductPricing(SalesOrderItem item, OCBCOrderProductTableTransaction bcOrderProduct , OCBCOrderTableTransaction bcOrder) {
	    	log.info("Setting Product Pricing for SKU: " + bcOrderProduct.getSku());
	    	PricingElement productPricingElement = new PricingElement();
	    	productPricingElement.setConditionType(secrects.getConditionTypePricing());
	    	BigDecimal price = bcOrderProduct.getBasePrice(); 
	    	BigDecimal quantity = new BigDecimal(bcOrderProduct.getQuantity());
	    	BigDecimal total = price.multiply(quantity);
	    	productPricingElement.setConditionRateValue(total.toString());
	    	productPricingElement.setConditionQuantityUnit(secrects.getConditionQuantityUnitDiscount());
	    	productPricingElement.setConditionCalculationType(secrects.getConditionCalculationTypePricing());
	    	productPricingElement.setConditionQuantity(GuidantConstants.INTEGERONE);
	    	productPricingElement.setPricingDateTime(SAPDateUtils.sanitizeDate(bcOrder.getOrderCreatedDate()));
	    	return productPricingElement;
	    }
	    
	    public PricingElement setProductDiscount(SalesOrderItem item, OCBCOrderProductTableTransaction bcOrderProduct , OCBCOrderTableTransaction bcOrder) {
	    	log.info("Setting Product Discount for SKU: " + bcOrderProduct.getSku());
	    	PricingElement productDiscountElement = new PricingElement();
	        productDiscountElement.setConditionType(secrects.getConditionTypeDiscount()); 
	        productDiscountElement.setConditionRateValue(calculateDiscountPercentage(bcOrderProduct)); 
	    	productDiscountElement.setConditionQuantityUnit(secrects.getConditionQuantityUnitDiscount());
	    	productDiscountElement.setConditionCalculationType(secrects.getConditionCalculationTypeDiscount());
	    	productDiscountElement.setConditionQuantity(GuidantConstants.INTEGERONE);
	        productDiscountElement.setPricingDateTime(SAPDateUtils.sanitizeDate(bcOrder.getOrderCreatedDate()));
	    	return productDiscountElement;
	    }
	    
	    public String calculateDiscountPercentage(OCBCOrderProductTableTransaction bcOrderProduct) {
	        if (bcOrderProduct.getCouponDiscountAmount() == null || 
	            bcOrderProduct.getCouponDiscountAmount().compareTo(BigDecimal.ZERO) <= 0) {
	            return "0.00";
	        }

	        BigDecimal price = bcOrderProduct.getBasePrice();
	        if (price == null || price.compareTo(BigDecimal.ZERO) == 0) {
	            return "0.00"; // Avoid divide-by-zero
	        }

	        BigDecimal discount = bcOrderProduct.getCouponDiscountAmount();
	        BigDecimal discountPercentage = discount
	                .divide(price, 4, RoundingMode.HALF_UP) // keep precision
	                .multiply(BigDecimal.valueOf(100))
	                .setScale(2, RoundingMode.HALF_UP); // round to 2 decimal places

	        return "-"+discountPercentage.toString();
	    }
		
	    public void setShipToPartyID(SalesOrder salesOrder , String customerShipto , String shippingAddressShipTo) {
	    	if(shippingAddressShipTo != null && !shippingAddressShipTo.isEmpty()&&!shippingAddressShipTo.equals(customerShipto)) {
	    		
	    	    SalesOrderPartner salesPartner = new SalesOrderPartner();
	    	    salesPartner.setCustomer(shippingAddressShipTo);
	    	    salesPartner.setPartnerFunction(GuidantConstants.SHIPTO);
	    	    List<SalesOrderPartner> salesPartners = new ArrayList<SalesOrderPartner>();
	    	    salesPartners.add(salesPartner);
	    	    SalesOrderPartnerWrapper salesOrderPartnerWrapper = new SalesOrderPartnerWrapper();
	    	    salesOrderPartnerWrapper.setResults(salesPartners);
	    	    salesOrder.setToPartner(salesOrderPartnerWrapper);
	    	}
	    }
}
