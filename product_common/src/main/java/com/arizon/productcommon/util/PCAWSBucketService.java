/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.util;

/**
 *
 * @author kavinkumar.s
 */
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.arizon.productcommon.config.PCConstants;
import static com.arizon.productcommon.config.PCConstants.secrets;
import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.service.PCProductEmailService;
import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Configuration
@Service
@Slf4j
public class PCAWSBucketService {

    @Autowired
    PCProductEmailService emailService;

    @Autowired
    PCProductCommonConfig config;
    
    public String saveToS3Bucket(String activeProfile, String region, String accessKey, String secretKey, File reportFile, String fileName) {
        try {

            String bucketPath = secrets.getS3BucketName() + "/" + secrets.getProductBukcetFolder();
            Optional<AmazonS3> optionalClient = getS3Client(activeProfile, region, accessKey, secretKey);
            if (optionalClient.isPresent()) {
                AmazonS3 s3Client = optionalClient.get();
                PutObjectRequest request = new PutObjectRequest(bucketPath, fileName, reportFile);
                log.info("request" + request);
                s3Client.putObject(request);
                log.info("excelUploadToAwsS3 ends");
                return generateDownloadlinkURL(fileName, s3Client);
            }

            return "Unable to Find AmazonS3 client ";
        } catch (Exception e) {
            log.error("Exception on saveToS3Bucket method  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "saveToS3Bucket", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    private Optional<AmazonS3> getS3Client(String activeProfile, String region, String accessKey, String secretKey) {
        AmazonS3 s3client = null;
        try {
            log.info("getS3Client starts");

            if (activeProfile.equals("local")) {
                s3client = AmazonS3Client.builder()
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                        .withRegion(region)
                        .build();
            } else {
                s3client = AmazonS3Client.builder()
                        .withRegion(region)
                        .build();
            }
            return Optional.of(s3client);
        } catch (Exception e) {
            log.error("AwsBucketService.getS3Client :" + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "AwsBucketService.getS3Client", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return Optional.empty();
        }
    }

    private String generateDownloadlinkURL(String fileName, AmazonS3 s3Client) {
        try {
            String keyName = secrets.getProductBukcetFolder()+"/reports/" + fileName;
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(secrets.getS3BucketName(), keyName);
            ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
            overrides.setContentDisposition("attachment; filename=" + fileName);
            req.setResponseHeaders(overrides);

            URL url = s3Client.generatePresignedUrl(req);
            String Downloadurl = url.toString();
            log.info("Downloadurl :" + Downloadurl);
            return Downloadurl;
        } catch (Exception e) {
            log.error("Exception on generateDownloadlinkURL method  :  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "AwsBucketService.generateDownloadlinkURL", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }

    }
    
    public List<String> getS3ProductFileContent(String accessKey, String secretKey, String region, String activeProfile, String processType) throws IOException {
 
        log.info("Entered into AWSBucketService.getS3ProductFileContent method");
        List<String> productDetails = new ArrayList<>();
        Optional<AmazonS3> s3Client = this.getS3Client(activeProfile, region, accessKey, secretKey);
        AmazonS3 amazonS3 = s3Client.get();
        List<S3ObjectSummary> s3ObjectSummaryList;
        if (processType.equalsIgnoreCase(PCConstants.PENDING)) {
            s3ObjectSummaryList = amazonS3.listObjects(secrets.getS3BucketName(), secrets.getProductBukcetFolder() + "/pending").getObjectSummaries();
        } else {
            s3ObjectSummaryList = amazonS3.listObjects(secrets.getS3BucketName(), secrets.getProductBukcetFolder() + "/failed").getObjectSummaries();
        }
        log.info("s3ObjectSummaryList  :  " + s3ObjectSummaryList);
        S3Object object;
        BufferedReader bufferReader = null;
        StringBuilder contentBuilder;
        log.info("Total no of s3Object in " + processType + " folder : " + s3ObjectSummaryList.size());
        for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaryList) {
            object = amazonS3.getObject(secrets.getS3BucketName(), s3ObjectSummary.getKey());
            if (s3ObjectSummary.getKey().endsWith("/")) {
                continue;
            }
            String fileName = s3ObjectSummary.getKey().split("/")[(s3ObjectSummary.getKey().split("/").length) - 1];
            try (InputStreamReader streamReader = new InputStreamReader(object.getObjectContent(), StandardCharsets.UTF_8)) {
                log.info("File in process : " + s3ObjectSummary.getKey());
 
                bufferReader = new BufferedReader(streamReader);
                contentBuilder = new StringBuilder();
                String currentLine;
                while ((currentLine = bufferReader.readLine()) != null) {
                    contentBuilder.append(currentLine).append("\n");
                }
                bufferReader.close();
                productDetails.add(contentBuilder.toString());
                moveS3File(s3ObjectSummary.getKey(), secrets.getProductBukcetFolder() + "/success/" +fileName, fileName, amazonS3);
            } catch (Exception e) {
                log.error("Exception on AWSBucketService.getS3ProductFileContent method : " + ProductUtility.getStackTrace(e));
                moveS3File(s3ObjectSummary.getKey(), secrets.getProductBukcetFolder() + "/failed/" + fileName, fileName, amazonS3);
                emailService.sendErrorMail(secrets.getClientName(), "AWSBucketService.getS3ProductFileContent", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            }
        }
 
        return productDetails;
    }
    
    private String moveS3File(String sourceKey, String destinationKey, String fileName, AmazonS3 s3Client) {
        log.info("Entered into AWSBucketService.moveS3File method ");
        try {
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(secrets.getS3BucketName(), sourceKey, secrets.getS3BucketName(), destinationKey);
            s3Client.copyObject(copyObjRequest);
            boolean isFileCopied = s3Client.doesObjectExist(secrets.getS3BucketName(), destinationKey);
            if (isFileCopied) {
                s3Client.deleteObject(secrets.getS3BucketName(), sourceKey);
                emailService.sendBucketStatusMail(fileName, PCConstants.SUCCESS, destinationKey, secrets.getClientName(), PCConstants.s3SubjectMail, config.getPrefix());
            } else {
                emailService.sendBucketStatusMail(fileName, PCConstants.FAILED, destinationKey, secrets.getClientName(), PCConstants.s3SubjectMail, config.getPrefix());
            }
 
            return PCConstants.SUCCESS;
        } catch (SdkClientException e) {
            log.error("Exception on AWSBucketService.moveS3File method : " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "AwsBucketService.moveS3File", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }
    
}
