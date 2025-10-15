/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.arizon.productcommon.model.FilterEntry;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author mohan.e
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCAwsSecrets {

    private String db_url;
    private String db_username;
    private String db_password;
    private String bcBaseUrl;
    private String storehash;
    private String clientId;
    private String accessToken;
    private String period;
    private String max_period;
    private String max_attempts;
    private String timeout_period;
    private String hostMail;
    private String port;
    private String SMTPuser;
    private String SMTPpass;
    private String clientName;
    private String s3BucketName;
    private String reports_folder;
    private String customerBukcetFolder;
    private String orderBukcetFolder;
    private String productBukcetFolder;
    private String inventoryBukcetFolder;
    private String shipmentBukcetFolder;
    private String pricelistBukcetFolder;
    private String customerHeaders;
    private String orderHeaders;
    private String queueURL;
    private String errorQueueURL;
    private String fromMail;
    private String developerMailTo;
    private String reportMailTo;
    private String productHeader;
    private String nsConsumeKey;
    private String nsConsumerSecret;
    private String nsTokenID;
    private String nsTokenSecret;
    private String nsOrderUrl;
    private String nsInventoryUrl;
    private String nsProductUrl;
    private String nsRealm;
    private String executionTime;
    private int item_buffer_quantity;
    private int customerApiSchedular;
    private int shipmentApiSchedular;
    private int productApiSchedular;
    private Integer macModBufferedQuantity;
    private String macModDefaultLeadTime;
    private String categoryQueueURL;
    private String categoryErrorQueueURL;
    private String klevuAPIAuthKey;
//    private String mailPrefix;
}
