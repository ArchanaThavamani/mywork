package com.arizon.racetrac.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "blueyonderOrderClient",
    url = "https://api.jdadelivers.com"
)
public interface BlueyonderOrderClient {

    @PostMapping(
        value = "/dp/interactive/orderReleases/v1",
        consumes = "application/json"
    )
    ResponseEntity<String> postOrder(
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody String orderJson
    );
}
