/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.service;

import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.entity.OCBCWebhookDetailsTransaction;
import com.arizon.ordercommons.repository.OCBCWebhookDetailsTransactionRepository;
import com.arizon.ordercommons.restclient.OCBigcommerceClient;
import com.arizon.ordercommons.restclient.OCRestClientBuilder;
import com.arizon.ordercommons.util.OrderUtility;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Service
@Slf4j
public class OCWebHookServices {

    @Autowired
    private OCBCWebhookDetailsTransactionRepository webHookRepo;

    @Autowired
    private OCRestClientBuilder clientBuilder;

    @Autowired
    private OCEmailServices emailService;

    public String fetchWebhookDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(OCConstants.currentDateFormat);
        LocalDateTime now = LocalDateTime.now();
        String curDate = dtf.format(now);
        try {
            List<OCBCWebhookDetailsTransaction> webHooksDetails = webHookRepo.findByisActive(true);
            String mailContent;
            int currentActiveCount = 0;
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            for (OCBCWebhookDetailsTransaction webHookDetail : webHooksDetails) {
                try {
                    log.info("WebHook Scope  : " + webHookDetail.getScope());
                    log.info("WebHook ID     : " + webHookDetail.getBcWebhookID());
                    feign.Response feignResponse = bcRestClientBuilder.getWebhook(webHookDetail.getBcWebhookID(), secrects.getAccessToken(), secrects.getStorehash());
                    if (feignResponse.status() == 200) {
                        String bcWebHookData = IOUtils.toString(feignResponse.body().asReader());
                        JSONObject webHookJson = new JSONObject(bcWebHookData);
                        if (!webHookJson.getJSONObject("data").getBoolean("is_active")) {
                            JSONObject updateJson = webHookJson.getJSONObject("data");
                            updateJson.put("is_active", true);
                            currentActiveCount += updateWebhookStatus(updateJson.toString(), webHookDetail);
                        } else {
                            currentActiveCount++;
                        }

                    } else {
                        emailService.sendErrorMail("fetchWebhookDetails", IOUtils.toString(feignResponse.body().asReader()), secrects.getClientName() + OCConstants.developerMailSub + curDate);
                    }
                } catch (Exception e) {
                    emailService.sendErrorMail("orderCommons.WebHookServices.fetchWebhookDetails", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
                }
            }
            mailContent = "Hi,  <br><br>"
                    + "Please find the status of Webhooks  -  " + secrects.getClientName() + " below, <br>"
                    + "<br>"
                    + "Here's the details.<br><br>"
                    + "Total Active Webhooks    : " + webHooksDetails.size() + " <br>"
                    + "Current Active Webhooks  : " + currentActiveCount + " <br>"
                    + "In Active Webhooks       : " + (webHooksDetails.size() - currentActiveCount) + " <br>"
                    + "<br>"
                    + "Thanks" + "<br>"
                    + "Arizon";

            emailService.sendWebhookReportMail(curDate, mailContent);
            return OCConstants.success;
        } catch (JSONException e) {
            log.error("Exception on orderCommons.WebHookServices.fetchWebhookDetails  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("orderCommons.WebHookServices.fetchWebhookDetails", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());

            return OCConstants.failed;
        }
    }

    private int updateWebhookStatus(String jsonRequest, OCBCWebhookDetailsTransaction webHookDetail) {
        log.info("orderCommons.WebHookServices.updateWebhookStatus method started  ");
        try {
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            feign.Response feignResponse = bcRestClientBuilder.updateWebhook(jsonRequest, webHookDetail.getBcWebhookID(), secrects.getAccessToken(), secrects.getStorehash());

            if (feignResponse.status() == 200) {
                log.info("Webhook activated successfully");
                return 1;
            } else {
                emailService.sendErrorMail("getBCOrderData", "Scope  :  " + webHookDetail.getScope() + " " + IOUtils.toString(feignResponse.body().asReader()), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
                return 0;
            }
        } catch (IOException e) {
            log.error("Exception on orderCommons.WebHookServices.updateWebhookStatus  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("orderCommons.WebHookServices.updateWebhookStatus", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return 0;
        }
    }
}
