package com.arizon.racetrac_salesorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arizon.racetrac_salesorder.services.SalesOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api")

public class SalesOrderController {
	@Autowired
	SalesOrderService SalesOrderService;
	
	@GetMapping("/salesorders")
	public String integrateSalesOrder() {
		SalesOrderService.integrateSalesOrdersToSAP();
		return "SUCCESS";
	}

}
