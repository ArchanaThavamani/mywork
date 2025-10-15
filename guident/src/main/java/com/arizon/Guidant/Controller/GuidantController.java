package com.arizon.Guidant.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.arizon.Guidant.GuidantConstants;
import com.arizon.Guidant.model.sap.BusinessPartnerDTO;
import com.arizon.Guidant.model.sap.BusinessPartnerResponse;
import com.arizon.Guidant.model.sap.BusinessPartnerResults;
//import com.arizon.Guidant.model.sap.BusinessPartnerDTO;
//import com.arizon.Guidant.model.sap.SAPBusinessPartnerResponseWrapper;
import com.arizon.Guidant.services.GuidantBusinessPartner;
import com.arizon.Guidant.services.GuidantOrderService;



import com.arizon.Guidant.services.SAPOrderService;

import com.arizon.Guidant.services.SAPProductService;
import com.arizon.Guidant.util.GuidantUtil;
import com.arizon.customercommon.controller.AdisToBcCustomer;
import com.arizon.customercommon.model.BCCustomerDTO;
import com.arizon.customercommon.service.BcCustomerServices;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.controller.OCBigCommerceToAdis;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.productcommon.config.PCConstants;
import com.arizon.productcommon.controller.PCBCProductsController;


import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Lazy
public class GuidantController {
	
	

    @Autowired
    OCBigCommerceToAdis ocBigCommerceToAdis;


    @Autowired
    OCBCOrderService ocbcOrderService;
    
    @Autowired 
    SAPOrderService sapService;
    
    
    @Autowired 
    GuidantOrderService guidantOrderService;

    @Autowired 
    GuidantBusinessPartner sapBusinessPartnerService;
    

    @Autowired 
    BcCustomerServices bcCustomerServices;
    
    @Autowired
    SAPProductService sapProductService;
    
    @Autowired
    PCBCProductsController bcProductController;
    
    
    @Autowired
    private AdisToBcCustomer adisToBcCustomer;

//    @GetMapping("/load/order")
//    public String loadOrder() {
//        return guidantOrderService.process();
//    }
    
    @GetMapping("/ordertest")
    public String getCommonOrder() {
        ocbcOrderService.fetchWebHookPendingOrders(OCConstants.PendingStatus, OCConstants.NORMAL_PROCESS);
        return GuidantConstants.SUCCESS;
    
    }
    
  
    
	@GetMapping("/salesorders")
	public String integrateSalesOrder() {
		guidantOrderService.integrateSalesOrdersToSAP();
		return GuidantConstants.SUCCESS;
	}
    
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("==> Test endpoint hit");
        return ResponseEntity.ok("Test Success");
    }

    @GetMapping("/business-partners")
	public String getBusinessPartners() {
        sapBusinessPartnerService.integrateBusinessPartners();		
        return GuidantConstants.SUCCESS; 
	}

    @Synchronized
    @GetMapping("/guidant/postcustomer/{status}")
    public String integrateCoreCustomerToBc(@PathVariable String status) {
        try {
            log.info("Guidant -> CustomerController.integrateCoreCustomerToBc started");

            // Step 1: Push only customer master data from ADIS to BigCommerce

            adisToBcCustomer.createUpdateCustomer("nhrwflyaph", status);

        } catch (Exception e) {
            log.error("Exception on Guidant.CustomerController.integrateCoreCustomerToBc ", e);
            return OCConstants.failed;
        }   

        log.info("Guidant -> CustomerController.integrateCoreCustomerToBc ended");
        return OCConstants.success;
    }

 

    @GetMapping("/BCCustomer/{customerId}")
   	public BCCustomerDTO getBCCustomerById(@PathVariable Integer customerId ) {
    	Optional<BCCustomerDTO> customerOpt = bcCustomerServices.getCustomerFromBC(customerId);
    	if (customerOpt.isPresent()) {
    	    BCCustomerDTO customer = customerOpt.get();
    	    return customer;
    	} else {
    	    log.warn("No customer found for ID {}",customerId);
    	}
    	return null;
   	}
    
    @GetMapping("/SAPProducts")
    public String fetchProductsFromSAP() {
		sapProductService.fetchProductsFromSAP();
		return GuidantConstants.SUCCESS;
	}
    
    @GetMapping("/ADISToBCProducts")
    public String integrateProductsToBC() {
    	bcProductController.getProductsFromAdis(PCConstants.INSERT);
    	bcProductController.getProductsFromAdis(PCConstants.UPDATE);
    	return GuidantConstants.SUCCESS;
    }
}

