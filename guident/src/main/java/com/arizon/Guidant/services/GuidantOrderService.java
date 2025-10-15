package com.arizon.Guidant.services;



import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.config.GuidantAwsSecrets;
import com.arizon.Guidant.exception.GuidantException;
import com.arizon.Guidant.restclient.GuidantRestClientBuilder;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.customercommon.model.BCCustomerAttributeDTO;
import com.arizon.customercommon.model.BCCustomerDTO;
import com.arizon.customercommon.service.BcCustomerServices;
import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.controller.OCBigCommerceToAdis;
import com.arizon.ordercommons.entity.OCBCOrderProductTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;
import com.arizon.ordercommons.entity.OCBCShippingAddressTransaction;
import com.arizon.ordercommons.entity.OCBCTrackingTableTransaction;
import com.arizon.ordercommons.model.OrderWebhookStatusDTO;
import com.arizon.ordercommons.repository.OCBCOrderProductTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderShippingAddressTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionRepository;
import com.arizon.ordercommons.repository.OCBCTrackingTransactionRepository;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.service.OCEmailServices;
import com.arizon.ordercommons.util.OrderUtility;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class GuidantOrderService {

    @Autowired
    OCBigCommerceToAdis ocBigCommerceToAdis;

    @Autowired
    OCBCOrderService ocbcOrderService;

    @Autowired
    OCAwsSecrets ocAwsSecrets ;

    @Autowired
    OCBCOrderTransactionRepository ocbcOrderTransactionRepository;

    @Autowired
    OCBCOrderProductTransactionRepository ocbcOrderProductTransactionRepository;

    @Autowired
    GuidantRestClientBuilder guidantRestClientBuilder;

    @Autowired
    GuidantAwsSecrets guidantAwsSecrets;

//    @Autowired
//    PCBigcommerceRestService pcBigcommerceRestService;

    @Autowired
    OCBCTrackingTransactionRepository ocbcTrackingTransactionRepository;

    @Autowired
    OCEmailServices ocEmailServices;
    
    @Autowired
    SAPOrderService sapOrderService;
    
    @Autowired
    SAPRestService sapRestService;
    
    @Autowired
    BcCustomerServices bcCustomerServices;
    
    @Autowired
    OCBCOrderShippingAddressTransactionRepository ocBCOrderShippingAddressTransactionRepository;

   
    @Value("${spring.profiles.active}")
    String activeProfile;
    @Value("${cloud.aws.region.static}")
    String region;
    @Value("${cloud.aws.secretName}")
    String secretName;
    @Value("${cloud.aws.credentials.access_key}")
    String accessKey;
    @Value("${cloud.aws.credentials.secret_key}")
    String secretKey;
    
    public String integrateSalesOrdersToSAP() {
		log.info("Startring Sales Order Integration process");
		try {
			processOrderWebhook();
		    integrateOrder();
		} catch (Exception e) {
			log.info("Exception in processing the order: " + GuidantUtil.getStackTrace(e));
			return OCConstants.failed;
		}
		log.info("Order integration completed successfully");
		return OCConstants.success;
	}
    

    public String  processOrderWebhook() {
        String rtnStatus = null;

        try {
            log.info("Order webhook process started");
            String queueStatus = ocBigCommerceToAdis.fetchOrderDetailsFromQueue(ocAwsSecrets.getStorehash(), accessKey, secretKey, region, activeProfile, ocAwsSecrets.getQueueURL(), ocAwsSecrets.getErrorQueueURL());
            if(queueStatus.equalsIgnoreCase(OCConstants.success)){
                OrderWebhookStatusDTO orderWebhookStatusDTO =  ocbcOrderService.fetchWebHookPendingOrders(OCConstants.PendingStatus, OCConstants.NORMAL_PROCESS);
                if(orderWebhookStatusDTO!= null &&  orderWebhookStatusDTO.getStatus() != null &&  orderWebhookStatusDTO.getStatus().equalsIgnoreCase(OCConstants.failed)){
                    log.info("Order process failed in ADIS");
                    return OCConstants.failed;
                }
            } 
            rtnStatus = OCConstants.success;
        } catch (Exception e) {
            log.error("OrderService.process Exception: " + GuidantUtil.getStackTrace(e));
            String statusDesc = "Error in Processing Order: " + e.getMessage();
            ocEmailServices.sendAlertMail(statusDesc + " " + OrderUtility.getCurrentData(),ocAwsSecrets.getClientName() + " Order Integration Failed",ocAwsSecrets.getDeveloperMailTo());
            rtnStatus = GuidantConstants.FAILED;
        }
        log.info("Webhook process completed with status: " + rtnStatus);
        return rtnStatus;
    }
    
    
    
    public List<OCBCOrderProductTableTransaction> getOrderProductList(Integer orderId) {
		List<OCBCOrderProductTableTransaction>  orderProductList = ocbcOrderProductTransactionRepository.findByorderId(orderId);
		return orderProductList;
	}  
    
    public OCBCShippingAddressTransaction getOrderShippingAddress(Integer orderId) {
        return ocBCOrderShippingAddressTransactionRepository.findByorderID(orderId)
                .orElseGet(() -> {
                    log.warn("Shipping address not found for order ID: {}", orderId);
                    return new OCBCShippingAddressTransaction();
                });
    }
    
//	public void integrateOrder() {
//		try {
//			List<OCBCOrderTableTransaction> ocbcOrderTableTransactionList = ocbcOrderTransactionRepository
//					.findByStorehashAndIntegrationStatus(ocAwsSecrets.getStorehash(), "Pending");
//			if (ocbcOrderTableTransactionList != null && !ocbcOrderTableTransactionList.isEmpty()) {
//				for (OCBCOrderTableTransaction ocbcOrderTableTransaction : ocbcOrderTableTransactionList) {
//					
//					sapOrderService.transformBCOrder(ocbcOrderTableTransaction,
//							getOrderProductList(ocbcOrderTableTransaction.getOrderId()));
//				}
//			}
//		} catch (Exception e) {
//			log.info("Error in integrating order: " + GuidantUtil.getStackTrace(e));
//		}
//	}
	
	public void integrateOrder() {
		try {
			List<OCBCOrderTableTransaction> ocbcOrderTableTransactionList = ocbcOrderTransactionRepository
					.findByStorehashAndIntegrationStatus(ocAwsSecrets.getStorehash(), "Pending");

			if (ocbcOrderTableTransactionList != null && !ocbcOrderTableTransactionList.isEmpty()) {
				for (OCBCOrderTableTransaction orderTransaction : ocbcOrderTableTransactionList) {
					try {
						List<OCBCOrderProductTableTransaction> productList = getOrderProductList(orderTransaction.getOrderId());
						OCBCShippingAddressTransaction shippingAddressTransaction = getOrderShippingAddress(orderTransaction.getOrderId());
						String transformResponse = sapOrderService.transformBCOrder(orderTransaction, productList , shippingAddressTransaction);
						if (OCConstants.failed.equals(transformResponse)) {
							log.info("Order transform failed for orderId: {}", orderTransaction.getOrderId());
						} 
						else {
						    String postOrderStatus = sapRestService.postSalesOrders(transformResponse ,orderTransaction.getSourceOrderId() ,orderTransaction.getOrderId());
						    if (OCConstants.success.equals(postOrderStatus)) {
						    	setOrderStatus(OCConstants.completed, orderTransaction);
						    } else {
						    	log.info("Order posting failed for orderId: {}", orderTransaction.getOrderId());
						    	setOrderStatus(OCConstants.failed, orderTransaction);
						    }
						}
					} catch (Exception e) {
						log.info("Error processing orderId {}: {}", orderTransaction.getOrderId(), GuidantUtil.getStackTrace(e));
					}
				}
			}
		} catch (Exception e) {
			log.info("Exception  occured in integrateOrder: " + GuidantUtil.getStackTrace(e));
		}
	}
	
	
	public void setOrderStatus(String status, OCBCOrderTableTransaction orderTransaction) {
		try {
			log.info("Setting order status to: " + status + " for orderId: " + orderTransaction.getOrderId());
			orderTransaction.setIntegrationStatus(status);
			ocbcOrderTransactionRepository.save(orderTransaction);
		} catch (Exception e) {
			log.info("Error setting order status for orderId: " + orderTransaction.getOrderId() + " - "
					+ GuidantUtil.getStackTrace(e));

		}
	}
	
	
	

}
