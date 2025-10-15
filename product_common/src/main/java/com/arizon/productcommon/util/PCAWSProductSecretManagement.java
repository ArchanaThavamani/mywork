/*
 * To change license header, choose License Headers in Project Properties.
 * To change template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.arizon.productcommon.config.PCAwsSecrets;
import com.arizon.productcommon.config.PCConstants;
import static com.arizon.productcommon.config.PCConstants.secrets;
import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.service.PCProductEmailService;
import com.google.gson.Gson;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author sasikumar.a
 */
@Configuration
@ComponentScan
@Slf4j
@Component("AWSProductCommon")
@Service
public class PCAWSProductSecretManagement {
    
    @Autowired
    PCProductEmailService emailService;
    
    @Autowired
    PCProductCommonConfig config;

    private Gson gson = new Gson();
    
    public DataSource dataSource(String access_key, String secret_key, String region, String secretName, String activeProfile, String StoreHash) {
        try {
            log.info("AWSCommonSecretManagement.dataSource method has started :" + StoreHash+"\n");
            log.info("activeProfile  :  " + activeProfile + ": StoreHash :" + StoreHash);
            PCConstants.secrets = getSecret(access_key, secret_key, region, secretName, activeProfile, StoreHash);
            log.info("AWSCommonSecretManagement.dataSource method has ended :" + StoreHash);
        } catch (Exception Ex) {
            log.error("Exception Occured in AWSCommonSecretManagement.dataSource method :" + Ex.toString() + ": StoreHAsh :" + StoreHash);
            emailService.sendErrorMail(secrets.getClientName(), "dataSource", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        return DataSourceBuilder.create().url(PCConstants.secrets.getDb_url()).username(PCConstants.secrets.getDb_username()).password(PCConstants.secrets.getDb_password()).build();

    }
  


    public PCAwsSecrets getSecret(String access_key, String secret_key, String region, String secretName, String activeProfile, String StoreHash) {
        PCAwsSecrets getData = null;
        AWSSecretsManager client;
        log.info("AWSCommonSecretManagement.getSecret method has started :" + StoreHash);
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

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (getSecretValueResult.getSecretString() != null) {
                secret = getSecretValueResult.getSecretString();
                log.info("The json from the AWS Secret are retrieved successfully ");
                getData = gson.fromJson(secret, PCAwsSecrets.class);
                return getData;
            }
            getData = gson.fromJson(secret, PCAwsSecrets.class);
            log.info("AWSCommonSecretManagement.getSecret method has ended :" + StoreHash);
        } catch (Exception e) {
            log.info("Exception-> getSecret " + e.getMessage());
            emailService.sendErrorMail(secrets.getClientName(), "getSecret", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        return getData;
    }

}
