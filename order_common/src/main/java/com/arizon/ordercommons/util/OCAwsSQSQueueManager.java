/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.model.OrderWebHookDTO;
import com.arizon.ordercommons.service.OCBCOrderService;
import com.arizon.ordercommons.service.OCEmailServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Component
@Service
@Configuration
@Slf4j
public class OCAwsSQSQueueManager {

    @Autowired
    OCBCOrderService bcOrderService;

    @Autowired
    private OCEmailServices emailService;

    private Optional<AmazonSQS> getSqsConnection(String accessKey, String secretKey, String region, String activeProfile) {
        AmazonSQS sqsClient;
        try {
            log.info("AwsSQSQueueManager.getSqsConnection connection method started :");
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            if (activeProfile.equals("local")) {
                sqsClient = AmazonSQSClient.builder().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                        .withRegion(region)
                        .build();
            } else {
                sqsClient = AmazonSQSClient.builder().standard()
                        .withRegion(region).build();
            }
            log.info("AwsSQSQueueManager.getSqsConnection connection method ended :");
            return Optional.of(sqsClient);
        } catch (Exception e) {
            log.error("Exception occur while connecting Amazon SQS service : " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AwsSQSQueueManager.getSqsConnection", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return Optional.empty();
        }
    }

    public String getQueueMessages(List<Message> messageList, String storeHash, String accessKey, String secretKey, String region, String activeProfile, String primaryQueueURL, String secondaryQueueURL) {
        List<Message> messages = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("AwsSQSQueueManager.getQueueMessages initialize this method from fetchOrderDetailsFromQueue :" + storeHash);
            AmazonSQS sqsClient;
            log.info("Trying to connect the SQS connection by call this method :" + storeHash);
            Optional<AmazonSQS> optionalSqs = getSqsConnection(accessKey, secretKey, region, activeProfile);
            if (optionalSqs.isEmpty()) {
                log.info("SQS connection Failed :" + storeHash);
                return OCConstants.failed;
            } else {
                sqsClient = optionalSqs.get();
                ReceiveMessageRequest messageRequest = new ReceiveMessageRequest().withQueueUrl(primaryQueueURL).withMaxNumberOfMessages(10).withVisibilityTimeout(90);
                messages = sqsClient.receiveMessage(messageRequest).getMessages();
                log.info("Check whether we received message from AWS SQS :");
                if (messages.size() > 0) {
                    log.info("Succesfully message received from SQS :" + storeHash);
                    for (Message message : messages) {
                        try {
                            String queueResponse = message.getBody();
                            OrderWebHookDTO orderResponse = mapper.readValue(queueResponse, new TypeReference<OrderWebHookDTO>() {
                            });
                            log.info("Order response from QUEUE which we converted after string to Object :" + orderResponse.toString());
                            boolean adisResponseForWebhook = bcOrderService.storeQueueResponseInAdis(orderResponse, OCConstants.PendingStatus);
                            if (adisResponseForWebhook) {
                                deleteSqsQueue(message, accessKey, secretKey, region, activeProfile, primaryQueueURL);
                            } else {
                                boolean response = moveOneSqsToAnotherSQS(message.getBody(), accessKey, secretKey, region, activeProfile, secondaryQueueURL);
                                if (response) {
                                    deleteSqsQueue(message, accessKey, secretKey, region, activeProfile, primaryQueueURL);
                                }
                            }
                            log.info("Message which we received from AWS SQS :" + queueResponse);
                            messageList.add(message);
                        } catch (JsonProcessingException e) {
                            log.error("Exception on getting queue messages from SQS " + OrderUtility.getStackTrace(e));
                            emailService.sendErrorMail("getting queue messages from SQS method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
                        }
                    }
                    OCConstants.emptyQueueCount = 0;
                } else {
                    log.info("We Didn't received any message from AWS SQS :" + storeHash);
                    OCConstants.emptyQueueCount += 1;
                }
            }
            log.info("AwsSQSQueueManager.getQueueMessages method ended :" + storeHash);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Exception on getting queue messages from SQS " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("getting queue messages from SQS method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return "FAILURE";
        }

    }

    public String deleteSqsQueue(Message message, String accessKey, String secretKey, String region, String activeProfile, String queueURL) {
        try {
            AmazonSQS sqsClient;
            log.info("AwsSQSQueueManager.deleteSqsQueue method started :");
            Optional<AmazonSQS> optionalSqs = getSqsConnection(accessKey, secretKey, region, activeProfile);
            if (optionalSqs.isEmpty()) {
                log.info("We didn't able to connect AWS SQS :");
                return OCConstants.failed;
            } else {
                sqsClient = optionalSqs.get();
                log.info("We delete this message from QUEUE after succesfully created in ADIS :" + message.toString());
                sqsClient.deleteMessage(queueURL, message.getReceiptHandle());
                return OCConstants.success;
            }
        } catch (Exception e) {
            log.error("Exception on deletingSQSQueue  : " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AwsSQSQueueManager.deletingSQSQueue", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    public boolean moveOneSqsToAnotherSQS(String MessageBody, String accessKey, String secretKey, String region, String activeProfile, String queueURL) {
        try {
            log.info("AwsSQSQueueManager.moveOneSqsToAnotherSQS method started :");
            AmazonSQS sqsClient;
            Optional<AmazonSQS> optionalSqs = getSqsConnection(accessKey, secretKey, region, activeProfile);
            if (optionalSqs.isEmpty()) {
                log.info("We didn't able to connect AWS SQS :");
                return false;
            } else {
                sqsClient = optionalSqs.get();
                SendMessageRequest send_msg_request;
                send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueURL)
                        .withMessageGroupId("1")
                        .withMessageDeduplicationId("ErrorOrder")
                        .withMessageBody(MessageBody);
                sqsClient.sendMessage(send_msg_request);
            }
            log.info("AwsSQSQueueManager.moveOneSqsToAnotherSQS method ended :");
            return true;
        } catch (Exception Ex) {
            log.error("Exception Occured in AwsSQSQueueManager.moveOneSqsToAnotherSQS method :" + Ex.toString());
            emailService.sendErrorMail("AwsSQSQueueManager.moveOneSqsToAnotherSQS method", Ex.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return false;
        }

    }
}
