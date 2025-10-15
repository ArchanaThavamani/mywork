package com.arizon.Guidant.services;

import static com.arizon.productcommon.config.PCConstants.secrets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.model.sap.ProductPlant;
import com.arizon.Guidant.model.sap.ProductSalesDelivery;
import com.arizon.Guidant.model.sap.SAPProduct;
import com.arizon.Guidant.model.sap.SAPProductResponse;
import com.arizon.Guidant.restclient.SAPAuthClient;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.productcommon.config.PCConstants;
import com.arizon.productcommon.entity.PCProductCustomFieldTransaction;
import com.arizon.productcommon.entity.PCProductTransaction;
import com.arizon.productcommon.repository.PCProductCustomFieldTransactionRepository;
import com.arizon.productcommon.repository.PCProductTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.arizon.ordercommons.configs.OCConstants.secrects;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SAPProductService {
	
	@Autowired
	private SAPRestService sapRestService;
	
	@Autowired
	PCProductTransactionRepository pcProductRepo; 
	
	@Autowired
	PCProductCustomFieldTransactionRepository customfieldMappingRepo;
	
	String productStatusInADIS = "";
	
	public void fetchProductsFromSAP() {
		try {
		    String productsResponseFromSAP =	sapRestService.fetchSAPProducts();
		    if(OCConstants.failed.equals(productsResponseFromSAP)) {
		    	log.info("Failed to fetch products from SAP");
		    } else {
		    	log.info("Successfully fetched products from SAP");
		    	ObjectMapper objectMapper = new ObjectMapper();
		    	SAPProductResponse productResponse = objectMapper.readValue(productsResponseFromSAP, SAPProductResponse.class);
		    	
//		    	List<SAPProduct> products = productResponse.getD().getResults();
//		    	SAPProduct product = products.get(0);
		    	storeProductsToADIS(productResponse);
		    	
		    }
		}
		catch(Exception e) {
			log.info("Exception  occured in fetchProductsFromSAP: " + e.getMessage()+GuidantUtil.getStackTrace(e));
		}
	}
	
	private void storeProductsToADIS(SAPProductResponse productResponse) {
		try {
			List<SAPProduct> products = productResponse.getD().getResults();
			if(products == null || products.isEmpty()) {
				log.info("No products found in SAP response");
				return ;
			} 
			for (SAPProduct product : products) {
				try {
					log.info("Processing product: " + product.getProduct());
					// Add logic to store product to ADIS here
					String crossPlantStatus = product.getCrossPlantStatus();
					if (crossPlantStatus != null && Arrays.asList(GuidantConstants.SKIP_STATUSES).contains(crossPlantStatus)) {
					    log.info("Skipping product " + product.getProduct() +
					             " due to crossPlantStatus: " + crossPlantStatus);
					    checkCrossPlantStatusProduct(product);
					    continue;
					}
					Integer productId = setProductFields(product);
					if (productId == null || productId <= 0) {
						log.info("Failed to set product fields for product: " + product.getProduct());
						continue;
					}
					setProductPlantCustomFields(product, productId);
					setSalesDeliveryCustomFields(product, productId);
				} catch (Exception e) {
					log.info("Exception  occured while processing product: " + product.getProduct() + " Exception: "
							+ e.getMessage() + GuidantUtil.getStackTrace(e));
				}
			}
		} catch (Exception e) {
			log.info("Exception  occured in storeProductsToADIS: " + e.getMessage()+GuidantUtil.getStackTrace(e));
		}
	}
	
	private Integer setProductFields(SAPProduct product) {
		try {
			
			log.info("Mapping SAP product fields to ADIS for product: " + product.getProduct());
			
			if(product.getToDescription() == null || product.getToDescription().getResults() == null || product.getToDescription().getResults().isEmpty()) {
				log.info("No product description found for product: " + product.getProduct());
				return -1;
			}
			String sapProductName =  product.getToDescription().getResults().get(0).getProductDescription();
			PCProductTransaction pcProduct ;
			Optional<PCProductTransaction> productInADIS = pcProductRepo.findByNameAndIsActive(sapProductName, true);
		    if(productInADIS.isPresent()) {
		    	log.info("Product already exists in ADIS: " + sapProductName);
		    	pcProduct = productInADIS.get();
		    	pcProduct.setProductStatus(PCConstants.UPDATE);
		    } else {
		    	log.info("Inserting new product to ADIS: " + sapProductName);
		    	pcProduct = new PCProductTransaction();
		    	pcProduct.setProductStatus(PCConstants.INSERT);
		    }	
			pcProduct.setName(sapProductName);
			pcProduct.setSku(product.getProduct());
			pcProduct.setWeight(Double.valueOf(product.getNetWeight()));
			pcProduct.setPrice(BigDecimal.valueOf(0.1));
		    pcProduct.setDescription(sapProductName);
		    pcProduct.setType(GuidantConstants.PHYSICALTYPE);
		    pcProduct.setActive(true);
		    pcProduct.setStorehash(secrets.getStorehash());
		    PCProductTransaction savedProduct = pcProductRepo.save(pcProduct);
		    productStatusInADIS = pcProduct.getProductStatus();
		    return savedProduct.getProductId();
		} catch (Exception e) {
			log.info("Exception  occured in setProductFields: " + e.getMessage()+GuidantUtil.getStackTrace(e));
			return -1;
		}
	}
	
	private void setProductPlantCustomFields(SAPProduct product , Integer productId) {
		try {
			
		    
			List<ProductPlant> productPlants =	product.getToPlant().getResults();
			
			List<PCProductCustomFieldTransaction> productCustomFields = customfieldMappingRepo.findByProductIdAndIsActive(BigInteger.valueOf(productId), true);
			
			for(PCProductCustomFieldTransaction customField : productCustomFields) {
				customField.setActive(false);
				//customField.setStatus(PCConstants.UPDATE);
			}
		    
		    for(ProductPlant plant : productPlants) {
		    	Optional<PCProductCustomFieldTransaction> customFieldOpt = customfieldMappingRepo.findByProductIdAndNameAndValue(BigInteger.valueOf(productId),GuidantConstants.PRODUCTPLANT,plant.getPlant());
		    	if(customFieldOpt.isPresent()) {
		    		PCProductCustomFieldTransaction customField = customFieldOpt.get();
		    		customField.setActive(true); 
		    		customField.setStatus(productStatusInADIS);
                    customfieldMappingRepo.save(customField);
		    	} else {
		    		PCProductCustomFieldTransaction customField = new PCProductCustomFieldTransaction();
		    		customField.setProductId(BigInteger.valueOf(productId));
		    		customField.setName(GuidantConstants.PRODUCTPLANT);
		    		customField.setValue(plant.getPlant());
		    		customField.setActive(true); 	
					customField.setStatus(productStatusInADIS);
					customfieldMappingRepo.save(customField);
		    	}
		    }
			
		} catch (Exception e) {
			log.info("Exception  occured in setProductCustomFields: " + e.getMessage()+GuidantUtil.getStackTrace(e));
		}
	}
	
	
	private void setSalesDeliveryCustomFields(SAPProduct product , Integer productId) {
		try {
			
			List<ProductSalesDelivery> salesDeliveryItems = product.getToSalesDelivery().getResults();
			if(salesDeliveryItems == null || salesDeliveryItems.isEmpty()) {
				log.info("No sales delivery data found for product: " + product.getProduct());
				return ;
			}
			salesDeliveryItems = salesDeliveryItems.stream().filter(item -> item.getProductDistributionChnl() != null && item.getProductDistributionChnl().equals(secrects.getDistributionChannel())).toList();
			for(ProductSalesDelivery salesDelivery : salesDeliveryItems) {
				setSalesOrganisationCustomFields(salesDelivery,productId,product);
				setProductPriceGroupCustomFields(salesDelivery,productId,product);
			}
		} catch (Exception e) {
			log.info("Exception  occured in setProductPriceGroupCustomFields: " + e.getMessage()+GuidantUtil.getStackTrace(e));
		}		
	}
	
	private void setSalesOrganisationCustomFields(ProductSalesDelivery salesDelivery ,Integer productId , SAPProduct product) {
		try {
			if(salesDelivery.getProductSalesOrg() == null || salesDelivery.getProductSalesOrg().isEmpty()) {
				return ;
			}
			Optional<PCProductCustomFieldTransaction> customFieldOpt = customfieldMappingRepo.findByProductIdAndNameAndValue(BigInteger.valueOf(productId),GuidantConstants.PRODUCTSALESORGANISATION,salesDelivery.getProductSalesOrg());
	    	if(customFieldOpt.isPresent()) {
	    		PCProductCustomFieldTransaction customField = customFieldOpt.get();
	    		customField.setActive(true);
	    		customField.setStatus(productStatusInADIS);
                customfieldMappingRepo.save(customField);
	    	} else {
	    		PCProductCustomFieldTransaction customField = new PCProductCustomFieldTransaction();
	    		customField.setProductId(BigInteger.valueOf(productId));
	    		customField.setName(GuidantConstants.PRODUCTSALESORGANISATION);
	    		customField.setValue(salesDelivery.getProductSalesOrg());
	    		customField.setActive(true); 	
				customField.setStatus(productStatusInADIS);
				customfieldMappingRepo.save(customField);
	    	}
		} catch (Exception e) {
			log.info("Exception  occured while processing sales organisation for product: " + product.getProduct() + " Exception: "
					+ e.getMessage() + GuidantUtil.getStackTrace(e));
		}
	}
	
	private void setProductPriceGroupCustomFields(ProductSalesDelivery salesDelivery ,Integer productId , SAPProduct product) {
		try {
			if(salesDelivery.getPriceSpecificationProductGroup() == null || salesDelivery.getPriceSpecificationProductGroup().isEmpty()) {
				return ;
			}
			Optional<PCProductCustomFieldTransaction> customFieldOpt = customfieldMappingRepo.findByProductIdAndNameAndValue(BigInteger.valueOf(productId),GuidantConstants.PRODUCTPRICEGROUP,salesDelivery.getPriceSpecificationProductGroup());
	    	if(customFieldOpt.isPresent()) {
	    		PCProductCustomFieldTransaction customField = customFieldOpt.get();
	    		customField.setActive(true); 	
	    		customField.setStatus(productStatusInADIS);
                customfieldMappingRepo.save(customField);
	    	} else {
	    		PCProductCustomFieldTransaction customField = new PCProductCustomFieldTransaction();
	    		customField.setProductId(BigInteger.valueOf(productId));
	    		customField.setName(GuidantConstants.PRODUCTPRICEGROUP);
	    		customField.setValue(salesDelivery.getProductSalesOrg() +"_"+salesDelivery.getPriceSpecificationProductGroup());
	    		customField.setActive(true); 	
				customField.setStatus(productStatusInADIS);
				customfieldMappingRepo.save(customField);
	    	}
		} catch (Exception e) {
			log.info("Exception  occured while processing price group for product: " + product.getProduct() + " Exception: "
					+ e.getMessage() + GuidantUtil.getStackTrace(e));
		}
	}
	
	public void checkCrossPlantStatusProduct(SAPProduct product) {
		try {
			Optional<PCProductTransaction> productInADIS = pcProductRepo.findBySkuAndIsActive(product.getProduct(),true);
			if (productInADIS.isPresent()) {
				PCProductTransaction pcProduct = productInADIS.get();
				pcProduct.setVisible(false);
				pcProduct.setProductStatus(PCConstants.UPDATE);
				pcProductRepo.save(pcProduct);
				log.info("Product " + product.getProduct() + " deactivated in ADIS.");
			} else {
				log.info("Product " + product.getProduct() + " not found in ADIS to deactivate.");
			}

		} catch (Exception e) {
			log.info("Exception  occured in checkCrossPlantStatusProduct: " + e.getMessage()
					+ GuidantUtil.getStackTrace(e));
		}

	}
	
	

}
