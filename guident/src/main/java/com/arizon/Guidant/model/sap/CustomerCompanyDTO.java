package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCompanyDTO {
    @JsonProperty("Customer")
    private String Customer;

    @JsonProperty("CompanyCode")
    private String CompanyCode;

    @JsonProperty("APARToleranceGroup")
    private String APARToleranceGroup;

    @JsonProperty("AccountByCustomer")
    private String AccountByCustomer;

    @JsonProperty("AccountingClerk")
    private String AccountingClerk;

    @JsonProperty("AccountingClerkFaxNumber")
    private String AccountingClerkFaxNumber;

    @JsonProperty("AccountingClerkInternetAddress")
    private String AccountingClerkInternetAddress;

    @JsonProperty("AccountingClerkPhoneNumber")
    private String AccountingClerkPhoneNumber;

    @JsonProperty("AlternativePayerAccount")
    private String AlternativePayerAccount;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;

    @JsonProperty("CollectiveInvoiceVariant")
    private String CollectiveInvoiceVariant;

    @JsonProperty("CustomerAccountNote")
    private String CustomerAccountNote;

    @JsonProperty("CustomerHeadOffice")
    private String CustomerHeadOffice;

    @JsonProperty("CustomerSupplierClearingIsUsed")
    private boolean CustomerSupplierClearingIsUsed;

    @JsonProperty("HouseBank")
    private String HouseBank;

    @JsonProperty("InterestCalculationCode")
    private String InterestCalculationCode;

    @JsonProperty("InterestCalculationDate")
    private String InterestCalculationDate;

    @JsonProperty("IntrstCalcFrequencyInMonths")
    private String IntrstCalcFrequencyInMonths;

    @JsonProperty("IsToBeLocallyProcessed")
    private boolean IsToBeLocallyProcessed;

    @JsonProperty("ItemIsToBePaidSeparately")
    private boolean ItemIsToBePaidSeparately;

    @JsonProperty("LayoutSortingRule")
    private String LayoutSortingRule;

    @JsonProperty("PaymentBlockingReason")
    private String PaymentBlockingReason;

    @JsonProperty("PaymentMethodsList")
    private String PaymentMethodsList;

    @JsonProperty("PaymentReason")
    private String PaymentReason;

    @JsonProperty("PaymentTerms")
    private String PaymentTerms;

    @JsonProperty("PaytAdviceIsSentbyEDI")
    private boolean PaytAdviceIsSentbyEDI;

    @JsonProperty("PhysicalInventoryBlockInd")
    private boolean PhysicalInventoryBlockInd;

    @JsonProperty("ReconciliationAccount")
    private String ReconciliationAccount;

    @JsonProperty("RecordPaymentHistoryIndicator")
    private boolean RecordPaymentHistoryIndicator;

    @JsonProperty("UserAtCustomer")
    private String UserAtCustomer;

    @JsonProperty("DeletionIndicator")
    private boolean DeletionIndicator;

    @JsonProperty("CashPlanningGroup")
    private String CashPlanningGroup;

    @JsonProperty("KnownOrNegotiatedLeave")
    private String KnownOrNegotiatedLeave;

    @JsonProperty("ValueAdjustmentKey")
    private String ValueAdjustmentKey;

    @JsonProperty("CustomerAccountGroup")
    private String CustomerAccountGroup;

    @JsonProperty("to_CompanyText")
    private CompanyTextWrapper toCompanyText;

}












