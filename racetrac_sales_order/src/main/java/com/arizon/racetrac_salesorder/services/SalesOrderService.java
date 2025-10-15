package com.arizon.racetrac_salesorder.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.controller.OCBigCommerceToAdis;
import com.arizon.ordercommons.model.OrderWebhookStatusDTO;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.util.OrderUtility;
import com.arizon.racetrac_salesorder.util.RacetracUtil;

@Service
public class SalesOrderService {
	  private static final Logger log = LoggerFactory.getLogger(SalesOrderService.class);
	 
	  @Autowired
	    OCBCOrderService ocbcOrderService;

	    @Autowired
	    OCBigCommerceToAdis ocBigCommerceToAdis;
	    
	    @Autowired
	    OCAwsSecrets ocAwsSecrets ;


	  public String integrateSalesOrdersToSAP() {
			log.info("Startring Sales Order Integration process");
			try {
				processOrderWebhook();
			    //integrateOrder();
			} catch (Exception e) {
				log.info("Exception in processing the order: " + RacetracUtil.getStackTrace(e));
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
	            log.error("OrderService.process Exception: " + RacetracUtil.getStackTrace(e));
	            String statusDesc = "Error in Processing Order: " + e.getMessage();
	            //ocEmailServices.sendAlertMail(statusDesc + " " + OrderUtility.getCurrentData(),ocAwsSecrets.getClientName() + " Order Integration Failed",ocAwsSecrets.getDeveloperMailTo());
	            //rtnStatus = GuidantConstants.FAILED;
	        }
	        log.info("Webhook process completed with status: " + rtnStatus);
	        return rtnStatus;
	    }
	    
	    

}
