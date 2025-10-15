package com.arizon.Guidant.services;


import static com.arizon.ordercommons.configs.OCConstants.secrects;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.model.sap.SalesOrderResponse;
import com.arizon.Guidant.restclient.SAPAuthClient;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.Guidant.util.SAPDateUtils;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.service.OCEmailServices;
import com.arizon.ordercommons.service.OCOrderLogServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SAPRestService {
	
	
	 
	 @Autowired
	 OCOrderLogServices ocOrderLogServices;
	 
	 @Autowired
	 OCEmailServices mailService;
	 
	 @Autowired
	 OCBCOrderService ocbcOrderService;
	 
	 
	 
	 @Autowired(required= true)
	  private SAPAuthClient sapAuthClient;
	  
	  
	//Step 1: Get Bearer Token from SAP
	public String getBearerToken() {
       try {
           String basicAuth = "Basic " + Base64.getEncoder().encodeToString((secrects.getSapClientId() + ":" + secrects.getSapClientSecret()).getBytes());

           MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
           form.add("grant_type", "client_credentials");

           Map<String, Object> tokenResponse = sapAuthClient.getAccessToken(basicAuth, form);
           return "Bearer " + tokenResponse.get("access_token");
       } catch (Exception e) {
           throw new RuntimeException("Error while getting SAP Bearer Token: " + e.getMessage(), e);
       }
   }
	
	// Step 2: Get CSRF Token and Cookie
	public Map<String, String> getCsrfTokenAndCookie(String bearerToken) {
		try {
			String cookieHeader = secrects.getSapCookieHeader();
			ResponseEntity<String> response = sapAuthClient.getSalesOrders(bearerToken, cookieHeader);

			String csrfToken = response.getHeaders().getFirst("x-csrf-token");
			String cookie = response.getHeaders().getFirst("Set-Cookie");

			Map<String, String> tokenData = new HashMap<>();
			tokenData.put("csrfToken", csrfToken);
			tokenData.put("cookie", cookie);

			log.info("SAP CSRF Token generated successfully: {}", csrfToken);
			return tokenData;
		} catch (Exception e) {
			throw new RuntimeException("Error while fetching CSRF token and cookie: " + e.getMessage(), e);
		}
	}

	 public String postSalesOrders(String requestBody , int sourceOrderId , int orderId) {
	    	try {
	          // Step 1: Get Token
	    		// Step 1: Get Bearer Token
				String bearerToken = getBearerToken();

	          // Step 2: Call SalesOrder API
				Map<String, String> tokenData = getCsrfTokenAndCookie(bearerToken);
				String csrfToken = tokenData.get("csrfToken");
				String cookie = tokenData.get("cookie");
	          System.out.println("SAP Sales Orders Token Successfully Generated : \n" + csrfToken);
	        
	          ResponseEntity<String> postResponse = sapAuthClient.postSalesOrders(
	        		  bearerToken,
	        	      cookie, // Cookie from the GET response
	        	      csrfToken, // Pass this in a new parameter
	        	      requestBody
	        	);
	          log.info("Order is created successfully:"+postResponse.getBody());
	          if(postResponse.getStatusCode().is2xxSuccessful()) {
	        	  log.info("Sales Order posted successfully to SAP.");
	        	  ObjectMapper mapper = new ObjectMapper();
	        	  SalesOrderResponse salesOrderResponse = 
	        	          mapper.readValue(postResponse.getBody(), SalesOrderResponse.class);
	              String destinationSalesOrderId = salesOrderResponse.getD().getSalesOrderId();
	              if (destinationSalesOrderId == null || destinationSalesOrderId.isEmpty()) {
	                  log.error("SAP response is 200 but SalesOrder ID is missing. Response: {}", postResponse.getBody());
	                  ocOrderLogServices.storeOrderLogDetails(orderId, "", OCConstants.failed,
	                          requestBody, postResponse.getBody(),
	                          GuidantConstants.SAPORDERFAILURE, sourceOrderId, GuidantConstants.ORDERINTERFACE);
	                  return OCConstants.failed;
	              }
	              mailService.sendAlertMail("SAP Order Posted Successfully , SAP order Id: " + destinationSalesOrderId, secrects.getClientName() + " Order Integration success", secrects.getDeveloperMailTo());
	        	  ocOrderLogServices.storeOrderLogDetails(orderId,destinationSalesOrderId, OCConstants.success, requestBody, postResponse.getBody(), GuidantConstants.SAPORDERSUCCESS,sourceOrderId, GuidantConstants.ORDERINTERFACE);
	        	  return OCConstants.success;
	          } else {
	        	  ocOrderLogServices.storeOrderLogDetails(orderId, "", OCConstants.failed, requestBody, postResponse.getBody(), GuidantConstants.SAPORDERFAILURE,sourceOrderId, GuidantConstants.ORDERINTERFACE);
	        	  return OCConstants.failed;
	          } 
	    	} catch (FeignException e) {
	            String errorMessage = e.contentUTF8(); // raw JSON error from SAP
	            log.error("Error in postSalesOrders: {}", errorMessage);

	            try {
	                // Parse JSON to extract message
	                ObjectMapper mapper = new ObjectMapper();
	                JsonNode root = mapper.readTree(errorMessage);
	                String sapErrorMsg = root.path("error").path("message").path("value").asText();

	                // Send email with the SAP error
	                
	                mailService.sendAlertMail("SAP Order Posting Failed: " + sapErrorMsg, secrects.getClientName() + " Order Integration Failed", secrects.getDeveloperMailTo());
	                ocbcOrderService.changeOrderStatusBC(12, sourceOrderId);
                    ocOrderLogServices.storeOrderLogDetails(orderId, "", OCConstants.failed, requestBody, sapErrorMsg, GuidantConstants.SAPORDERFAILURE,sourceOrderId, GuidantConstants.ORDERINTERFACE);
	                log.error("SAP Error Message: {}", sapErrorMsg);

	            } catch (Exception parseEx) {
	                log.error("Failed to parse SAP error JSON", parseEx);
	            }

	            return OCConstants.failed;
	    	} catch (Exception e) {
	    		log.error("Error in postSalesOrders: " + e.getMessage() + GuidantUtil.getStackTrace(e));
	    		return OCConstants.failed;
	    	}
	    }
	
	 public String fetchSAPProducts() {
		 try {
	        String basicAuth = "Basic " + Base64.getEncoder()
	                .encodeToString((secrects.getSapClientId() + ":" + secrects.getSapClientSecret()).getBytes());

	        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	        form.add("grant_type", "client_credentials");

	        Map<String, Object> tokenResponse = sapAuthClient.getAccessToken(basicAuth, form);
	        String bearerToken = "Bearer " + tokenResponse.get("access_token");

	        String expandParams = "to_Description,to_Plant,to_SalesDelivery";

	        String dateFilter = "LastChangeDate ge datetime'"+SAPDateUtils.yesterdayDate()+"'";
	        
	        ResponseEntity<String> response = sapAuthClient.getSAPProducts(
	                bearerToken,
	                null,
	                expandParams,
	                10,
	                dateFilter
	        );
	        if (response.getStatusCode().is2xxSuccessful()) {	
	            log.info("Products fetched successfully from SAP: " + response.getBody());
	            return response.getBody();
	        } else {
	            log.error("Failed to fetch products from SAP. Status: " + response.getStatusCode());
	            return OCConstants.failed;
	        }
   
	    } catch (Exception e) {
	    	log.error("Error in fetchSAPProducts: " + e.getMessage() + GuidantUtil.getStackTrace(e));
	    	return OCConstants.failed;
	    }
	 }
	 
	 

}
