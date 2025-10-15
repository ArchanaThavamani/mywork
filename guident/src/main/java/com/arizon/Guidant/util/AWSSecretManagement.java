package com.arizon.Guidant.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.arizon.Guidant.config.GuidantAwsSecrets;
import com.arizon.customercommon.util.CCAWSCommonSecretManagement;
import com.arizon.ordercommons.util.OCAWSOrderSecretManagement;

import com.arizon.productcommon.util.PCAWSProductSecretManagement;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;


@Component
@Configuration
@Service
@Slf4j
public class AWSSecretManagement {

    @Value("${spring.profiles.active}")
    String activeProfile;
    @Value("${cloud.aws.region.static}")
    String region;
    @Value("${cloud.aws.secretName}")
    String secretName;
    @Value("${cloud.aws.credentials.access_key}")
    String accessKey;
    @Value("${cloud.aws.credentials.secret_key}")
    String secretKey;



    @Autowired
    OCAWSOrderSecretManagement ocawsOrderSecretManagement;

    
    
    @Autowired
    CCAWSCommonSecretManagement customerSecretManagement;
    
    @Autowired
    PCAWSProductSecretManagement pcawsProductSecretManagement;


    @Bean
    public DataSource dataSource() {

        pcawsProductSecretManagement.dataSource(accessKey, secretKey, region, secretName, activeProfile, "xxx");
    	
        customerSecretManagement.dataSource(accessKey, secretKey, region, secretName, activeProfile, "xxx");

        return ocawsOrderSecretManagement.dataSource(accessKey, secretKey, region, secretName, activeProfile, "xxx");
    }


    @Bean
    public GuidantAwsSecrets getGuidantSecret() {
        return getSecret(accessKey, secretKey, region, secretName, activeProfile, "xxx");
    }

    public GuidantAwsSecrets getSecret(String access_key, String secret_key, String region, String secretName, String activeProfile, String StoreHash) {
    	GuidantAwsSecrets getData = null;
        AWSSecretsManager client;
        log.info("AWSCommonSecretManagement.getSecret method has started :" + StoreHash);
        Gson gson = new Gson();

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
                getData = gson.fromJson(secret, GuidantAwsSecrets.class);
                return getData;
            }
            getData = gson.fromJson(secret, GuidantAwsSecrets.class);
            log.info("AWSCommonSecretManagement.getSecret method has ended :" + StoreHash);
        } catch (Exception e) {
            log.info("Exception-> getSecret " + e.getMessage());
     //       emailService.sendErrorMail(secrets.getClientName(), "getSecret", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        return getData;
    }

}
