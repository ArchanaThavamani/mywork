package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {
 @JsonProperty("Customer")
    private String Customer;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;

    @JsonProperty("BillingIsBlockedForCustomer")
    private String BillingIsBlockedForCustomer;

    @JsonProperty("CreatedByUser")
    private String CreatedByUser;

    @JsonProperty("CreationDate")
    private String CreationDate;

    @JsonProperty("CustomerAccountGroup")
    private String CustomerAccountGroup;

    @JsonProperty("CustomerClassification")
    private String CustomerClassification;

    @JsonProperty("CustomerFullName")
    private String CustomerFullName;

    @JsonProperty("BPCustomerFullName")
    private String BPCustomerFullName;

    @JsonProperty("CustomerName")
    private String CustomerName;

    @JsonProperty("BPCustomerName")
    private String BPCustomerName;

    @JsonProperty("DeliveryIsBlocked")
    private String DeliveryIsBlocked;

    @JsonProperty("FreeDefinedAttribute01")
    private String FreeDefinedAttribute01;

    @JsonProperty("FreeDefinedAttribute02")
    private String FreeDefinedAttribute02;

    @JsonProperty("FreeDefinedAttribute03")
    private String FreeDefinedAttribute03;

    @JsonProperty("FreeDefinedAttribute04")
    private String FreeDefinedAttribute04;

    @JsonProperty("FreeDefinedAttribute05")
    private String FreeDefinedAttribute05;

    @JsonProperty("FreeDefinedAttribute06")
    private String FreeDefinedAttribute06;

    @JsonProperty("FreeDefinedAttribute07")
    private String FreeDefinedAttribute07;

    @JsonProperty("FreeDefinedAttribute08")
    private String FreeDefinedAttribute08;

    @JsonProperty("FreeDefinedAttribute09")
    private String FreeDefinedAttribute09;

    @JsonProperty("FreeDefinedAttribute10")
    private String FreeDefinedAttribute10;

    @JsonProperty("NFPartnerIsNaturalPerson")
    private String NFPartnerIsNaturalPerson;

    @JsonProperty("OrderIsBlockedForCustomer")
    private String OrderIsBlockedForCustomer;

    @JsonProperty("PostingIsBlocked")
    private boolean PostingIsBlocked;

    @JsonProperty("Supplier")
    private String Supplier;

    @JsonProperty("CustomerCorporateGroup")
    private String CustomerCorporateGroup;

    @JsonProperty("FiscalAddress")
    private String FiscalAddress;

    @JsonProperty("Industry")
    private String Industry;

    @JsonProperty("IndustryCode1")
    private String IndustryCode1;

    @JsonProperty("IndustryCode2")
    private String IndustryCode2;

    @JsonProperty("IndustryCode3")
    private String IndustryCode3;

    @JsonProperty("IndustryCode4")
    private String IndustryCode4;

    @JsonProperty("IndustryCode5")
    private String IndustryCode5;

    @JsonProperty("InternationalLocationNumber1")
    private String InternationalLocationNumber1;

    @JsonProperty("InternationalLocationNumber2")
    private String InternationalLocationNumber2;

    @JsonProperty("InternationalLocationNumber3")
    private String InternationalLocationNumber3;

    @JsonProperty("NielsenRegion")
    private String NielsenRegion;

    @JsonProperty("PaymentReason")
    private String PaymentReason;

    @JsonProperty("ResponsibleType")
    private String ResponsibleType;

    @JsonProperty("TaxNumber1")
    private String TaxNumber1;

    @JsonProperty("TaxNumber2")
    private String TaxNumber2;

    @JsonProperty("TaxNumber3")
    private String TaxNumber3;

    @JsonProperty("TaxNumber4")
    private String TaxNumber4;

    @JsonProperty("TaxNumber5")
    private String TaxNumber5;

    @JsonProperty("TaxNumberType")
    private String TaxNumberType;

    @JsonProperty("VATRegistration")
    private String VATRegistration;

    @JsonProperty("DeletionIndicator")
    private boolean DeletionIndicator;

    @JsonProperty("ExpressTrainStationName")
    private String ExpressTrainStationName;

    @JsonProperty("TrainStationName")
    private String TrainStationName;

    @JsonProperty("CityCode")
    private String CityCode;

    @JsonProperty("County")
    private String County;

    @JsonProperty("BR_ICMSTaxPayerType")
    private String BR_ICMSTaxPayerType;

    @JsonProperty("to_CustomerCompany")
    private CustomerCompanyWrapper toCustomerCompany;

    @JsonProperty("to_CustomerSalesArea")
    private CustomerSalesAreaWrapper toCustomerSalesArea;
}
