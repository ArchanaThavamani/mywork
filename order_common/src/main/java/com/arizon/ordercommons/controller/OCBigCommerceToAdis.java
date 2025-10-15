/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.repository.OCBCOrderWebhookTransactionRepository;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.util.OCAwsSQSQueueManager;
import com.arizon.ordercommons.util.OrderUtility;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author kavinkumar.s
 */
@Component("BigCommerceToAdis")
@Primary
@Slf4j
@Service

public class OCBigCommerceToAdis {

    @Autowired
    OCBCOrderService orderservice;

    @Autowired
    OrderUtility util;

    @Autowired
    OCBCOrderWebhookTransactionRepository webRepo;

    @Autowired
    private OCAwsSQSQueueManager awsQueueManager;
    
 

    public String getCommonStatus() {
        log.info("common setup called");
        log.info("Secrets  :  " + OCConstants.secrects.toString());
        return "success";
    }

     public String fetchOrderDetailsFromQueue(String STOREHASH, String accessKey, String secretKey, String region, String activeProfile, String primaryQueueUrl, String secondaryQueueUrl) {
        log.info("BigCommerceToAdis.fetchOrderDetailsFromQueue method successfully initializes from the main application :" + STOREHASH);
        try {

            List<Message> messageList = new ArrayList<>();
            awsQueueManager.getQueueMessages(messageList, STOREHASH, accessKey, secretKey, region, activeProfile, primaryQueueUrl, secondaryQueueUrl);
            log.info("CompletableFuture Thread method will ended in fetchOrderDetailsFromQueue :" + STOREHASH);
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception found in fetchOrderDetailsFromQueue webhook" + e.getMessage());
            return OCConstants.failed;
        }
    }

    public String generateReportMail(String accessKey, String secretKey, String region, String activeProfile) {
        log.info("ordercommons.BigCommerceToAdis.generateReportMail  method started");
        try {
            return orderservice.fetchOrderReportData(accessKey, secretKey, region, activeProfile);
        } catch (Exception e) {
            log.error("Excpetion on ordercommons.BigCommerceToAdis.generateReportMail  " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }
}
