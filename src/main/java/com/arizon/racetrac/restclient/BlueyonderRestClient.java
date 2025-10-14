package com.arizon.racetrac.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arizon.racetrac.config.RacetracAwsSecrets;
import com.arizon.racetrac.model.TokenResponse;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Component
@Slf4j
public class BlueyonderRestClient {

    @Autowired
    private BlueyonderFeignClient blueyonderFeignClient;
    
    @Autowired
    private RacetracAwsSecrets racetracAwsSecrets;
        
    
     public String  getAccessToken() {
        //Access Token
        Map<String, String> form = Map.of(
                "grant_type", racetracAwsSecrets.getGrant_type_so(),
                "client_id", racetracAwsSecrets.getClient_id_so(),
                "client_secret", racetracAwsSecrets.getClient_secret_so(),
                "scope", racetracAwsSecrets.scope_so
        );

        TokenResponse tokenResponse =
                blueyonderFeignClient.getToken("application/json", form);
                log.info("tokenResponse"+tokenResponse);

        return tokenResponse.getAccessToken();
    }    

}
