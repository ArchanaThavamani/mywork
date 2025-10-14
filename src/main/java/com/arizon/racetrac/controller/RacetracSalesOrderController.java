package com.arizon.racetrac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arizon.productcommon.controller.PCBCProductsController;
import com.arizon.racetrac.config.RacetracAwsSecrets;
import com.arizon.racetrac.restclient.BlueyonderRestClient;
import com.arizon.racetrac.services.RacetracOrderService;
import com.arizon.racetrac.services.RacetracTestOrderService;
import com.arizon.racetrac.util.RacetracConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Lazy
public class RacetracSalesOrderController {

    @Autowired 
    PCBCProductsController pcBCProductsController;

    @Autowired  
    RacetracAwsSecrets racetracAwsSecrets;

    @Autowired
    RacetracOrderService racetracOrderService;

    @Autowired
    RacetracTestOrderService racetracTestOrderService;

    @Autowired
    BlueyonderRestClient blueyonderRestClient;
    
    @GetMapping("/salesorders")
	public String integrateSalesOrder() {
		racetracOrderService.integrateSalesOrdersToBlueyonder();
		return RacetracConstants.SUCCESS;
	}

    @GetMapping("test/salesorders")
	public String integrateTestSalesOrder() {
		racetracTestOrderService.integrateTestSalesOrdersToBlueyonder();
		return RacetracConstants.SUCCESS;
	}
}
