package com.arizon.racetrac.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.controller.OCBigCommerceToAdis;
import com.arizon.ordercommons.entity.OCBCOrderProductTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;
import com.arizon.ordercommons.entity.OCBCShippingAddressTransaction;
import com.arizon.ordercommons.model.OrderWebhookStatusDTO;
import com.arizon.ordercommons.repository.OCBCOrderProductTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionRepository;
import com.arizon.ordercommons.repository.OCBCTrackingTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderShippingAddressTransactionRepository;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.service.OCEmailServices;
import com.arizon.ordercommons.util.OrderUtility;
import com.arizon.racetrac.util.RacetracConstants;
import com.arizon.racetrac.util.RacetracUtil;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
@Slf4j

@Service
public class RacetracOrderService {

    @Autowired
    OCBigCommerceToAdis ocBigCommerceToAdis;

    @Autowired
    OCBCOrderService ocbcOrderService;

    @Autowired
    OCAwsSecrets ocAwsSecrets;

    @Autowired
    OCBCOrderTransactionRepository ocbcOrderTransactionRepository;

    @Autowired
    OCBCOrderProductTransactionRepository ocbcOrderProductTransactionRepository;

    @Autowired
    OCBCTrackingTransactionRepository ocbcTrackingTransactionRepository;

    @Autowired
    OCEmailServices ocEmailServices;

    @Autowired
    OCBCOrderShippingAddressTransactionRepository ocBCOrderShippingAddressTransactionRepository;
    
    @Autowired
    BigcommerceOrderTransform bigcommerceOrderTransform;

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

    public String integrateSalesOrdersToBlueyonder() {
        log.info("Startring Sales Order Integration process");

        try {
            processOrders();
           // integrateOrder();
        } catch (Exception e) {
            log.info("Exception in processing the order: " + RacetracUtil.getStackTrace(e));
            return OCConstants.failed;
        }

        log.info("Order integration completed successfully");
        return OCConstants.success;
    }

    public String processOrders() {
        String rtnStatus = null;

        try {
            log.info("Order webhook process started");
            log.info("Storehash: "+ ocAwsSecrets.getStorehash()+"access key : "+
                accessKey+ "Secret key: "+
                secretKey+ " Region: "+
                region+ " Active Profile: "+
                activeProfile+ " getqueueurl: "+
                ocAwsSecrets.getQueueURL()+ "getErrorQueueURL: "+
                ocAwsSecrets.getErrorQueueURL());

            String queueStatus = ocBigCommerceToAdis.fetchOrderDetailsFromQueue(
                ocAwsSecrets.getStorehash(),
                accessKey,
                secretKey,
                region,
                activeProfile,
                ocAwsSecrets.getQueueURL(),
                ocAwsSecrets.getErrorQueueURL()
            );
            log.info("Queue Status: "+queueStatus);

            if (queueStatus.equalsIgnoreCase(OCConstants.success)) {
                OrderWebhookStatusDTO orderWebhookStatusDTO = ocbcOrderService.fetchWebHookPendingOrders(
                    OCConstants.PendingStatus,
                    OCConstants.NORMAL_PROCESS
                );

                if (orderWebhookStatusDTO != null &&
                    orderWebhookStatusDTO.getStatus() != null &&
                    orderWebhookStatusDTO.getStatus().equalsIgnoreCase(OCConstants.failed)) {

                    log.info("Order process failed in ADIS");
                    return OCConstants.failed;
                }
            }

            rtnStatus = OCConstants.success;

        } catch (Exception e) {
            log.error("OrderService.process Exception: " + RacetracUtil.getStackTrace(e));

            String statusDesc = "Error in Processing Order: " + e.getMessage();
            ocEmailServices.sendAlertMail(
                statusDesc + " " + OrderUtility.getCurrentData(),
                ocAwsSecrets.getClientName() + " Order Integration Failed",
                ocAwsSecrets.getDeveloperMailTo()
            );

            rtnStatus = RacetracConstants.FAILED;
        }

        log.info("Webhook process completed with status: " + rtnStatus);
        return rtnStatus;
    }
    
	public void integrateOrder() {
		try {
			List<OCBCOrderTableTransaction> ocbcOrderTableTransactionList = ocbcOrderTransactionRepository
					.findByStorehashAndIntegrationStatus(ocAwsSecrets.getStorehash(), "Pending");

			if (ocbcOrderTableTransactionList != null && !ocbcOrderTableTransactionList.isEmpty()) {
				for (OCBCOrderTableTransaction orderTransaction : ocbcOrderTableTransactionList) {
					try {
						List<OCBCOrderProductTableTransaction> productList = getOrderProductList(orderTransaction.getOrderId());
						OCBCShippingAddressTransaction shippingAddressTransaction = getOrderShippingAddress(orderTransaction.getOrderId());
						String transformResponse = bigcommerceOrderTransform.transformBCOrder(orderTransaction, productList , shippingAddressTransaction);
						if (OCConstants.failed.equals(transformResponse)) {
							log.info("Order transform failed for orderId: {}", orderTransaction.getOrderId());
						} 
//						else {
//						   // String postOrderStatus = SalesOrderRestServices.postSalesOrders(transformResponse ,orderTransaction.getSourceOrderId() ,orderTransaction.getOrderId());
//						    if (OCConstants.success.equals(postOrderStatus)) {
//						    	setOrderStatus(OCConstants.completed, orderTransaction);
//						    } else {
//						    	log.info("Order posting failed for orderId: {}", orderTransaction.getOrderId());
//						    	setOrderStatus(OCConstants.failed, orderTransaction);
//						    }
//						}
					} catch (Exception e) {
						log.info("Error processing orderId {}: {}", orderTransaction.getOrderId(), RacetracUtil.getStackTrace(e));
					}
				}
			}
		} catch (Exception e) {
			log.info("Exception  occured in integrateOrder: " + RacetracUtil.getStackTrace(e));
		}
	}
	
	
	public void setOrderStatus(String status, OCBCOrderTableTransaction orderTransaction) {
		try {
			log.info("Setting order status to: " + status + " for orderId: " + orderTransaction.getOrderId());
			orderTransaction.setIntegrationStatus(status);
			ocbcOrderTransactionRepository.save(orderTransaction);
		} catch (Exception e) {
			log.info("Error setting order status for orderId: " + orderTransaction.getOrderId() + " - "
					+ RacetracUtil.getStackTrace(e));

		}
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
    


}
