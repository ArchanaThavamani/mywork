package com.arizon.racetrac.restclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.arizon.racetrac.config.AuthClientConfig;
import com.arizon.racetrac.model.Racetrac_TokenResponse;


import org.springframework.http.MediaType;

@FeignClient(name="workdayaccesstokenrestclient",url="https://blueyonderus.b2clogin.com",configuration = AuthClientConfig.class)
public interface SalesOrderRestClient {
	 @PostMapping(value = "/blueyonderus.onmicrosoft.com/B2C_1A_ClientCredential/oauth2/v2.0/token?realmId=d5ae6e66-4a0a-4194-9a8f-0f2d40987a4f", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	 Racetrac_TokenResponse getAccessToken(
	            @RequestHeader("Accept") String accept,
	           
	            @RequestBody Map<String, ?> formParams
	    );

	 
}
