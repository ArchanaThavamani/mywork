/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.service.OCEmailServices;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Configuration
@ComponentScan
@Slf4j
@Service
public class OCAWSOrderSecretManagement {

    @Autowired
    private OCEmailServices emailService;

    private Gson gson = new Gson();

    public DataSource dataSource(String access_key, String secret_key, String region, String secretName, String activeProfile, String StoreHash) {
        try {
            log.info("AWSCommonSecretManagement.dataSource method started :" + StoreHash);
            log.info("activeProfile  :  " + activeProfile + ": StoreHAsh :" + StoreHash);
            OCConstants.secrects = getSecret(access_key, secret_key, region, secretName, activeProfile, StoreHash);
            log.info("AWSCommonSecretManagement.dataSource method started :" + StoreHash);
        } catch (Exception Ex) {
            log.error("Exception Occured in AWSCommonSecretManagement.dataSource method :" + Ex.toString() + ": StoreHAsh :" + StoreHash);
            emailService.sendErrorMail("AWSCommonSecretManagement.dataSource method", Ex.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());

        }
        return DataSourceBuilder.create().url(OCConstants.secrects.getDb_url()).username(OCConstants.secrects.getDb_username()).password(OCConstants.secrects.getDb_password()).build();

    }

    @Bean
    public OCAwsSecrets getSecret(String access_key, String secret_key, String region, String secretName, String activeProfile, String StoreHash) {
        OCAwsSecrets getData = null;
        AWSSecretsManager client;
        log.info("AWSCommonSecretManagement.getSecret method started :" + StoreHash);
        try {
            if (activeProfile.equals("local")) {
                client = AWSSecretsManagerClientBuilder.standard()
                        .withRegion(region)
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key)))
                        .build();
            } else {
                client = AWSSecretsManagerClientBuilder.standard()
                        .withRegion(region).build();
            }

            String secret = null;
            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                    .withSecretId(secretName);
            GetSecretValueResult getSecretValueResult = null;

            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (getSecretValueResult.getSecretString() != null) {
                secret = getSecretValueResult.getSecretString();
                log.info("secrets  :  " + secret);
                log.info("The json from the AWS Secret are retrieved successfully ");
                getData = gson.fromJson(secret, OCAwsSecrets.class);
                return getData;
            }
            getData = gson.fromJson(secret, OCAwsSecrets.class);
            log.info("AWSCommonSecretManagement.getSecret method started :" + StoreHash);
        } catch (JsonSyntaxException e) {
            log.info("Exception-> " + e.getMessage());
            emailService.sendErrorMail("AWSCommonSecretManagement.getSecrets method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());

        }
        return getData;
    }

}
