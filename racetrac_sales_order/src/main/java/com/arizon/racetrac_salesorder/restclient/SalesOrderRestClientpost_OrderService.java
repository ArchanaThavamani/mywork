package com.arizon.racetrac_salesorder.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.arizon.racetrac_salesorder.conig.AuthClientConfig;
import com.arizon.racetrac_salesorder.model.OrderRelease;

@FeignClient(name="workdayrestclient",url="https://api.jdadelivers.com",configuration = AuthClientConfig.class)
public interface SalesOrderRestClientpost_OrderService {

 
	@GetMapping("/dp/interactive/orderReleases/v1")
	ResponseEntity<String> getorder(
			

			 @RequestHeader("Authorization")String authorization);
	

}
