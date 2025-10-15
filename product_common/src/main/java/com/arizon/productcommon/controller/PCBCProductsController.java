/*;
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.controller;

import com.amazonaws.services.sqs.model.Message;
import com.arizon.productcommon.config.PCConstants;

import static com.arizon.productcommon.config.PCConstants.secrets;

import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.entity.PCProductTransaction;
import com.arizon.productcommon.repository.PCProductTransactionRepository;
import com.arizon.productcommon.service.PCAdisLogService;
import com.arizon.productcommon.service.PCBigCommerceProductService;
import com.arizon.productcommon.service.PCProductEmailService;
import com.arizon.productcommon.util.PCAWSProductSecretManagement;
import com.arizon.productcommon.util.PCAwsSQSQueueManager;
import com.arizon.productcommon.util.ProductUtility;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mohan.e
 */
@Component("PCAdisToBigcommerce")
@Primary
@Slf4j
@Service
public class PCBCProductsController {

    @Autowired
    PCProductTransactionRepository PCProductTransactionRepository;

    @Autowired
    PCBigCommerceProductService bcService;

    @Autowired
    PCAWSProductSecretManagement secretMananger;

    @Autowired
    PCProductEmailService emailService;

    @Autowired
    PCProductCommonConfig config;

    @Autowired
    PCAdisLogService logService;
    
    @Autowired
    PCAwsSQSQueueManager sqsQueueManager;

    @PostMapping("/productAdisToBc")
    public void getProductsFromAdis(String status) {
        try {
            log.info("product from Adis to Bc method started");
            String storehash = "";
            Integer BcProductId = 0;
            String categoryResult = "";
            String categoryMapping = "";
            String customResult = "";
            String brandResult = "";
            String productResult = "";
            //fetch the pending products from ADIS table
            log.info("AdisToBigCommerce.getProductsFromAdis method started" + storehash);
            List<PCProductTransaction> products = PCProductTransactionRepository.findByProductStatusAndStorehash(status, secrets.getStorehash());
            if (!products.isEmpty()) {

                for (PCProductTransaction product : products) {

                    BcProductId = product.getDestinationProductId();
                    if (BcProductId == null || BcProductId == 0) {
                        BcProductId = bcService.productCreate(product);
                        if (BcProductId != 0) {
                            categoryMapping = bcService.updateProductCategoryInBc(product.getProductId());
                            customResult = bcService.customFieldValidate(product.getProductId(), status);
                            if (product.getSku() == null || product.getSku().isEmpty()) {
                                log.info("Variant level product  :  " + product.getProductId());
                                bcService.fetchOptionSkuDetailsADIS(product, status);
                            }
                        }

                    } else {
                        // product delete scenario...
                        boolean IsDeleted = product.isDeleted();
                        if (IsDeleted) {
                            productResult = bcService.productDelete(BcProductId, storehash, product.getProductId());
                        } else {
                            // Product update scenarios...
                            bcService.updateProduct(BcProductId, product.getProductId(), "Full");
                            bcService.updateProductCategoryInBc(product.getProductId());
                            bcService.customFieldValidate(product.getProductId(), status);
                        }
                    }
                }

//                }
            } else {
                log.warn("we doesn't received any product from DataBase :");
            }
        } catch (Exception Ex) {
            log.error("Exception : getProductsFromAdis - " + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "getProductsFromAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }

    }

    @GetMapping("/generateReportEmail")
    public String generateReportMail(String accessKey, String secretKey, String region, String activeProfile) {
        try {
            logService.fetchProductReportData(accessKey, secretKey, region, activeProfile);
            return PCConstants.SUCCESS;
        } catch (Exception Ex) {
            log.error("Exception : generateReportMail - " + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "generateReportMail", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String oneTimeCategorySync() {
        try {
            bcService.fetchCategroyFromBC();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
    public String oneTreeTimeCategorySync() {
        try {
            bcService.fetchCategoryTreeFromBC();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String oneTimeproductSync() {
        try {
            bcService.fetchProductFromBC();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrating product from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
    public String syncContextFilters() {
        try {
            bcService.fetchFiltersFromBC();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrating filters from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
   
    public String fetchWebhookDetailsFromQueue(String STOREHASH, String accessKey, String secretKey, String region, String activeProfile, String primaryQueueUrl, String secondaryQueueUrl , String messageType) {
        log.info("BigCommerceToAdis.fetchWebhookDetailsFromQueue method successfully initializes from the main application :" + STOREHASH);
        try {

            List<Message> messageList = new ArrayList<>();
            sqsQueueManager.getQueueMessages(messageList, STOREHASH, accessKey, secretKey, region, activeProfile, primaryQueueUrl, secondaryQueueUrl, messageType);
            log.info("Completed fetchWebhookDetailsFromQueue processing for STOREHASH:" + STOREHASH);
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception found in fetchProductDetailsFromQueue webhook" + e.getMessage());
            return PCConstants.FAILED;
        }
    }
    
    
    
    
}
