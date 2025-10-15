/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kavinkumar.s
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OCAwsSecrets {

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

    private String inventoryBukcetFolder;
    private String pricelistBukcetFolder;
    private String customerHeaders;
    private String orderHeaders;
    private String queueURL;
    private String errorQueueURL;
    private String fromMail;
    private String clientMailTo;
    private String developerMailTo;
    private String reportMailTo;

    private String nsConsumeKey;
    private String nsConsumerSecret;
    private String nsTokenID;
    private String nsTokenSecret;
    private String nsOrderUrl;
    private String nsRealm;

    private String executionTime;
    public String invoiceBukcetFolder;
    public int blockedCustomerGroupID;
    public int directCustomerGroupID;
    public String geo_map_key;
    public int retryCount;
    public String orderStatus;
    public int soptype;
    public String taxdtlid;
    public int pymttype;
    public String docid;
    public boolean trdisamtspecified;
    public String bachnumb;
    public String curncyid;
    public String uofm;
    public int usingheaderleveltaxes;
    public String taxschid;
    public int createtaxes;
    public int item_buffer_quantity;
    public int guest_CustomerId;
    private String customerBukcetFolder;
    private String orderBukcetFolder;
    private String productBukcetFolder;
    private String shipmentBukcetFolder;
    private int customerApiSchedular;
    private int shipmentApiSchedular;
    private int productApiSchedular;
    public String destinationClientSystem;
    public String siteId;
    public String salesOrderType;
    public String salesOrderTypeInternalCode;
    public String salesOrganization;
    public String distributionChannel;
    public String organizationDivision;
    public String conditionTypePricing;
    public String conditionTypeDiscount;
    public String conditionCalculationTypePricing;
    public String conditionCalculationTypeDiscount;
    public String conditionQuantityUnitPricing;
    public String conditionQuantityUnitDiscount;
    public String accountingDocExternalReference;
    public String priceDetnExchangeRate;
    public String sapBaseUrl;
    public String sapClientSecret;
    public String sapClientId;
    public String sapCookieHeader;
    public Integer soldToPartyBCAttributeId;
    public Integer shipToPartyBCAttributeId;
    public Integer payerBCAttributeId;
    public Integer billToPartyBCAttributeId;
    public Integer salesOrganizationBCAttributeId;
}
