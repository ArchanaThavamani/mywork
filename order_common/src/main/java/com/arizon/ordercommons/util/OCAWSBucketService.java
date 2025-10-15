/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.util;

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
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.service.OCBucketMailServices;
import com.arizon.ordercommons.service.OCEmailServices;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
//import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
//import org.xml.sax.SAXException;

@Component("AwsBucket")
@Configuration
@Service
@Slf4j
public class OCAWSBucketService {

    @Autowired
    private OCEmailServices emailService;

    @Autowired
    private OCBucketMailServices bucketMailServices;

    public String saveToS3Bucket(String activeProfile, String region, String accessKey, String secretKey, File reportFile, String fileName) {
        try {
            log.info("Entered into the AWSBucketService.saveToS3Bucket method ");
            String bucketPath = secrects.getS3BucketName() +"/"+ secrects.getOrderBukcetFolder()+ "/reports";
            Optional<AmazonS3> optionalClient = getS3Client(activeProfile, region, accessKey, secretKey);
            if (optionalClient.isPresent()) {
                AmazonS3 s3Client = optionalClient.get();
                PutObjectRequest request = new PutObjectRequest(bucketPath, fileName, reportFile);
                s3Client.putObject(request);
                log.info("excelUploadToAwsS3 ends");
                String keyName = secrects.getOrderBukcetFolder()+ "/reports/" + fileName;
                return generateDownloadlinkURL(fileName, s3Client, keyName);
            }

            return "Unable to Find AmazonS3 client ";
        } catch (SdkClientException e) {
            log.error("Exception on saveToS3Bucket method  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AWSBucketService.saveToS3Bucket method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }
    }

    private Optional<AmazonS3> getS3Client(String activeProfile, String region, String accessKey, String secretKey) {

        try {
            log.info("getS3Client starts");
            AmazonS3 s3client;
            if (activeProfile.equals("local")) {
                s3client = AmazonS3Client.builder()
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                        .withRegion(region)
                        .build();
                return Optional.of(s3client);
            } else {
                s3client = AmazonS3Client.builder()
                        .withRegion(region)
                        .build();
                return Optional.of(s3client);
            }

        } catch (Exception e) {
            log.error("AwsBucketService.getS3Client :" + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AwsBucketService.getS3Client", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return Optional.empty();
        }
    }

    private String generateDownloadlinkURL(String fileName, AmazonS3 s3Client, String keyname) {
        try {
            log.info("Entered into the AWSBucketService.generateDownloadlinkURL method ");
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(secrects.getS3BucketName(), keyname);
            ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
            overrides.setContentDisposition("attachment; filename=" + fileName);
            req.setResponseHeaders(overrides);

            URL url = s3Client.generatePresignedUrl(req);
            String downloadUrl = url.toString();
            log.info("Downloadurl :" + downloadUrl);
            return downloadUrl;
        } catch (SdkClientException e) {
            log.error("Exception on generateDownloadlinkURL method  :  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("generateDownloadlinkURL method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }

    }

    public List<String> getS3FileContent(String accessKey, String secretKey, String region, String activeProfile, String orderPath, String processType) {

        List<String> orderDetails = new ArrayList<>();
        try {
            Optional<AmazonS3> s3Client = this.getS3Client(activeProfile, region, accessKey, secretKey);
            if (s3Client.isEmpty()) {
                return new ArrayList();
            }
            AmazonS3 amazonS3 = s3Client.get();
            List<S3ObjectSummary> s3ObjectSummaryList;
            if (processType.equalsIgnoreCase(OCConstants.PendingStatus)) {
                log.info("source path : " + secrects.getOrderBukcetFolder()+ orderPath + "Pending");
                s3ObjectSummaryList = amazonS3.listObjects(secrects.getS3BucketName(), orderPath + "Pending").getObjectSummaries();
//                s3ObjectSummaryList = amazonS3.listObjects("connolly-qa-data", "gp-integration/Order/BackOrder/" + "Pending").getObjectSummaries();
            } else {
                log.info("source path : " + secrects.getOrderBukcetFolder()+ orderPath + "Pending");
                s3ObjectSummaryList = amazonS3.listObjects(secrects.getS3BucketName(), orderPath + "Failure").getObjectSummaries();
            }
            log.info("s3ObjectSummaryList  :  " + s3ObjectSummaryList);
            log.info("Total no of s3Object in bucket : " + s3ObjectSummaryList.size());
            S3Object object;
            for (S3ObjectSummary s3ObjectSummary : s3ObjectSummaryList) {
                object = amazonS3.getObject(secrects.getS3BucketName(), s3ObjectSummary.getKey());
                if (s3ObjectSummary.getKey().endsWith("/")) {
                    continue;
                }
                BufferedReader bufferReader = null;
                StringBuilder contentBuilder;
                String fileName = s3ObjectSummary.getKey().split("/")[(s3ObjectSummary.getKey().split("/").length) - 1];
                try ( InputStreamReader streamReader = new InputStreamReader(object.getObjectContent(), StandardCharsets.UTF_8)) {
                    log.info("File in process : " + s3ObjectSummary.getKey());
                    bufferReader = new BufferedReader(streamReader);
                    contentBuilder = new StringBuilder();
                    Object currentLine;
                    while ((currentLine = bufferReader.readLine()) != null) {
                        log.info(currentLine.toString());
                        contentBuilder.append(currentLine).append("\n");
                    }
                    bufferReader.close();
                    orderDetails.add(contentBuilder.toString());
                    moveS3File(s3ObjectSummary.getKey(), orderPath + "Success/" + fileName, fileName, amazonS3);
                } catch (SdkClientException e) {
                    log.error("Exception on getS3FileContent method  :  " + OrderUtility.getStackTrace(e));
                    moveS3File(s3ObjectSummary.getKey(), orderPath + "Failure/" + fileName, fileName, amazonS3);
                    emailService.sendErrorMail("AWSBucketService.getS3FileContent", e.getLocalizedMessage(), secrects.getClientName() + OCConstants.developerMailSub);
                }

            }

            return orderDetails;
        } catch (Exception e) {
            log.error("Exception on AWSBucketService.getS3FileContent method : " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AWSBucketService.getS3FileContent", e.getLocalizedMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return orderDetails;
        }
    }

    private String moveS3File(String sourceKey, String destinationKey, String fileName, AmazonS3 s3Client) {
        log.info("Entered into AWSBucketService.moveS3File method ");
        try {
            log.info("source path : " + sourceKey);
            log.info("destination path : " + destinationKey);
            CopyObjectRequest copyObjRequest = new CopyObjectRequest(secrects.getS3BucketName(), sourceKey, secrects.getS3BucketName(), destinationKey);
            CopyObjectResult response = s3Client.copyObject(copyObjRequest);
            boolean isFileCopied = s3Client.doesObjectExist(secrects.getS3BucketName(), destinationKey);
            if (isFileCopied) {
                s3Client.deleteObject(secrects.getS3BucketName(), sourceKey);
                bucketMailServices.sendBucketStatusMail(fileName, OCConstants.success, destinationKey, secrects.getClientName() + OCConstants.developerMailSub);
            } else {
                bucketMailServices.sendBucketStatusMail(fileName, OCConstants.failed, destinationKey, secrects.getClientName() + OCConstants.developerMailSub);
            }
            return OCConstants.success;
        } catch (SdkClientException e) {
            log.error("Exception on AWSBucketService.moveS3File method : " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AWSBucketService.moveS3File", e.getLocalizedMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    public String writeXMLData(String activeProfile, String region, String accessKey, String secretKey, String XMLresposne) throws IOException {
        try {
            log.info("AWSBucketService.writeXMLData method started");
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String current_date_formatted = formatter.format(date);
            log.info("rawdate :" + current_date_formatted);
            String filename = "gp_xml_request_" + current_date_formatted + ".txt";
            log.info("XML File Name:" + filename);
            String filepath = secrects.getS3BucketName() + "/" + secrects.getOrderBukcetFolder()+ "/OrderXmlRequest/Pending";
            log.info("XML File path" + filepath);
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(XMLresposne)));
//
//            // Write the parsed document to an xml file
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            DOMSource source = new DOMSource(doc);
            File file = File.createTempFile("gp_xml_request", ".txt");
            //StreamResult result = new StreamResult(file);
            //transformer.transform(source, result);
            try ( java.io.FileWriter fw = new java.io.FileWriter(file)) {
                fw.write(XMLresposne);
                fw.close();
            }
            saveXmlToS3Bucket(activeProfile, region, accessKey, secretKey, file, filename);
        } catch (Exception e) {
            log.error("Exception occured when write  a xml data in s3 bucket" + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
        log.info("AWSBucketService.writeXMLData method ended");
        return OCConstants.success;
    }

    public String saveXmlToS3Bucket(String activeProfile, String region, String accessKey, String secretKey, File reportFile, String fileName) {
        try {
            log.info("Entered into the AWSBucketService.saveXmlToS3Bucket method ");
            String bucketPath = secrects.getS3BucketName() + "/" + secrects.getOrderBukcetFolder()+ "/OrderXmlRequest/Pending";
            Optional<AmazonS3> optionalClient = getS3Client(activeProfile, region, accessKey, secretKey);
            if (optionalClient.isPresent()) {
                AmazonS3 s3Client = optionalClient.get();
                PutObjectRequest request = new PutObjectRequest(bucketPath, fileName, reportFile);
                s3Client.putObject(request);
                log.info("excelUploadToAwsS3 ends");
                String keyName = secrects.getOrderBukcetFolder()+ "/OrderXmlRequest/Pending" + fileName;
                //generateDownloadlinkURL(fileName, s3Client, keyName);
                return OCConstants.success;
            }
            return "Unable to Find AmazonS3 client ";
        } catch (SdkClientException e) {
            log.error("Exception on saveToS3Bucket method  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("AWSBucketService.saveXmlToS3Bucket method", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }
    }
}
