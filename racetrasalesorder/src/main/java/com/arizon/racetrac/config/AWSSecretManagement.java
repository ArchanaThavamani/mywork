package com.arizon.racetrac.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.util.OCAWSOrderSecretManagement;
import com.arizon.productcommon.util.PCAWSProductSecretManagement;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class AWSSecretManagement {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.secretName}")
    private String secretName;

    @Value("${cloud.aws.credentials.access_key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret_key}")
    private String secretKey;

    @Autowired
    private PCAWSProductSecretManagement pcawsProductSecretManagement;

    @Autowired
    private OCAWSOrderSecretManagement ocawsOrderSecretManagement;

    @Bean
    public DataSource dataSource() {
        pcawsProductSecretManagement.dataSource(accessKey, secretKey, region, secretName, activeProfile, "xxx");
        return ocawsOrderSecretManagement.dataSource(accessKey, secretKey, region, secretName, activeProfile, accessKey);
    }

    @Bean
    public RacetracAwsSecrets racetracAwsSecrets() {
        return getSecret(accessKey, secretKey, region, secretName, activeProfile, "xxx");
    }

    private RacetracAwsSecrets getSecret(String accessKey,
                                         String secretKey,
                                         String region,
                                         String secretName,
                                         String activeProfile,
                                         String storeHash) {
        log.info("AWSSecretManagement.getSecret started for storeHash: {}", storeHash);
        AWSSecretsManager client;

        if ("local".equalsIgnoreCase(activeProfile)) {
            client = AWSSecretsManagerClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                    .build();
            log.info("Local profile is validated");
        } else {
            client = AWSSecretsManagerClientBuilder.standard()
                    .withRegion(region)
                    .build();
        }

        try {
            GetSecretValueRequest request = new GetSecretValueRequest().withSecretId(secretName);
            GetSecretValueResult result = client.getSecretValue(request);

            if (result.getSecretString() != null) {
                String secretJson = result.getSecretString();
                log.info("AWS secrets retrieved successfully");
                log.info("Secrets JSON: {}", secretJson);

                RacetracAwsSecrets secrets = new Gson().fromJson(secretJson, RacetracAwsSecrets.class);
                log.info("Mapped RacetracAwsSecrets: {}", secrets);
                return secrets;
            }
        } catch (Exception e) {
            log.error("Error retrieving AWS secret for storeHash {}: {}", storeHash, e.getMessage(), e);
        }

        return null;
    }

    @Primary
    @Bean
    public OCAwsSecrets ocAwsSecretsOverride(OCAWSOrderSecretManagement secretManagement) {
        log.info("Creating OCAwsSecrets bean manually to bypass autowiring issue...");
        return secretManagement.getSecret(accessKey, secretKey, region, secretName, activeProfile, "xxx");
    }
}
