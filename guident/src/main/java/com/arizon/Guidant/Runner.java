package com.arizon.Guidant;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.arizon.Guidant.Controller.GuidantController;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Runner implements CommandLineRunner {
	
	
	@Autowired
	GuidantController guidantController;
	
	 @Override
	    public void run(String... args) throws Exception {
	        log.info("args :" + Arrays.deepToString(args));
	        log.info("args" + args.length);
	        for (String batchName : args) {
	            log.info("BatchName :" + batchName);
	            switch (batchName) {

	                case "OrdersFromQueue":
	                    try {
	                    	guidantController.integrateSalesOrder();
	                    } catch (Exception e) {
	                        log.error("Error in OrdersFromQueue batch  method call : ", e);
	                    }
	                    break;
	                    
					case "ProductsFromSAPtoBC":
						try {
							guidantController.fetchProductsFromSAP();
							guidantController.integrateProductsToBC();
						} catch (Exception e) {
							log.error("Error in  ProductsFromSAPtoBC batch method call : ", e);
						}
						break;
	         
	                default:
	                    log.info("Batch name doesn't match our batch process name");
	                    break;
	            }
	        }
	        System.exit(0);
	    }	
}
