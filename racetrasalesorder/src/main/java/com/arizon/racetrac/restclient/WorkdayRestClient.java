package com.arizon.racetrac.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.arizon.racetrac.config.AuthClientConfig;
import com.arizon.racetrac.model.PurchaseItemResponse;
import com.arizon.racetrac.model.TokenResponse;

import java.util.Map;

@FeignClient (name = "workdayrestclient", url = "https://wd5-impl-services1.workday.com",configuration = AuthClientConfig.class)

public interface WorkdayRestClient {

	 @PostMapping(value = "/ccx/oauth2/racetrac9/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	    TokenResponse getToken(
	            @RequestHeader("Accept") String accept,
	           
	            @RequestBody Map<String, ?> formParams
	    );
	
	 @GetMapping("/api/wql/v1/racetrac9/data")
	 ResponseEntity<PurchaseItemResponse>  getPurchaseItems(
	        @RequestParam("query") String query,
	        @RequestHeader("Accept") String accept,
	        @RequestHeader("client_id") String clientId,
	        @RequestHeader("Authorization") String authorization
	    );
}