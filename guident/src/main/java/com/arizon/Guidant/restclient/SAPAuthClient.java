package com.arizon.Guidant.restclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.arizon.Guidant.config.SAPAuthClientConfig;



@FeignClient(name = "sapClient", url = "https://guidant.test.apimanagement.us10.hana.ondemand.com", configuration = SAPAuthClientConfig.class)
public interface SAPAuthClient {

	// Token Request
	@PostMapping(value = "/v1/generate_token", consumes = "application/x-www-form-urlencoded")
	Map<String, Object> getAccessToken(@RequestHeader("Authorization") String basicAuth,
			@RequestBody MultiValueMap<String, String> body);

	// Sales Order API
	@GetMapping(value = "/SalesOrder/QA/A_SalesOrder", headers = { "Content-Type=application/json",
			"Accept=application/json", "Accept-Encoding=*", "x-csrf-token=Fetch" })
	ResponseEntity<String> getSalesOrders(@RequestHeader("Authorization") String bearerToken,
			@RequestHeader("Cookie") String cookie);
	
	
	@PostMapping(value = "/SalesOrder/QA/A_SalesOrder", headers = { "Content-Type=application/json",
			"Accept=application/json", "Accept-Encoding=*" })
	ResponseEntity<String> postSalesOrders(@RequestHeader("Authorization") String bearerToken,
			@RequestHeader("Cookie") String cookie,  @RequestHeader("x-csrf-token") String csrfToken,@RequestBody String requestBody);

	@GetMapping(value = "/BusinessPartner/QA/A_BusinessPartner", headers = {
    "Content-Type=application/json",
    "Accept=application/json",
    "Accept-Encoding=*",
    "x-csrf-token=Fetch"
	})
	ResponseEntity<String> getBusinessPartners(
    	@RequestHeader("Authorization") String bearerToken,
    	@RequestHeader("Cookie") String cookie,
    	@RequestParam("$expand") String expand,
    	@RequestParam("$top") int top
	);
	

	
	@GetMapping(value = "/MaterialMaster/QA/A_Product", headers = {
			"Content-Type=application/json", "Accept=application/json", "Accept-Encoding=*", "x-csrf-token=Fetch" })
	ResponseEntity<String> getSAPProducts(
			@RequestHeader("Authorization") String bearerToken,
			@RequestHeader("Cookie") String cookie, 
			@RequestParam("$expand") String expand,
			@RequestParam("$top") int top,
			@RequestParam("$filter")String filter
			);



}

