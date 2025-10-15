package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSalesAreaDTO {
    
    @JsonProperty("Customer")
    private String Customer;

    @JsonProperty("SalesOrganization")
    private String SalesOrganization;

    @JsonProperty("DistributionChannel")
    private String DistributionChannel;

    @JsonProperty("Division")
    private String Division;

    @JsonProperty("AccountByCustomer")
    private String AccountByCustomer;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;

    @JsonProperty("BillingIsBlockedForCustomer")
    private String BillingIsBlockedForCustomer;

    @JsonProperty("CompleteDeliveryIsDefined")
    private boolean CompleteDeliveryIsDefined;

    @JsonProperty("CreditControlArea")
    private String CreditControlArea;

    @JsonProperty("Currency")
    private String Currency;

    @JsonProperty("CustIsRlvtForSettlmtMgmt")
    private boolean CustIsRlvtForSettlmtMgmt;

    @JsonProperty("CustomerABCClassification")
    private String CustomerABCClassification;

    @JsonProperty("CustomerAccountAssignmentGroup")
    private String CustomerAccountAssignmentGroup;

    @JsonProperty("CustomerGroup")
    private String CustomerGroup;

    @JsonProperty("CustomerIsRebateRelevant")
    private boolean CustomerIsRebateRelevant;

    @JsonProperty("CustomerPaymentTerms")
    private String CustomerPaymentTerms;

    @JsonProperty("CustomerPriceGroup")
    private String CustomerPriceGroup;

    @JsonProperty("CustomerPricingProcedure")
    private String CustomerPricingProcedure;

    @JsonProperty("CustomerStatisticsGroup")
    private String CustomerStatisticsGroup;

    @JsonProperty("CustProdProposalProcedure")
    private String CustProdProposalProcedure;

    @JsonProperty("DeliveryIsBlockedForCustomer")
    private String DeliveryIsBlockedForCustomer;

    @JsonProperty("DeliveryPriority")
    private String DeliveryPriority;

    @JsonProperty("IncotermsClassification")
    private String IncotermsClassification;

    @JsonProperty("IncotermsLocation2")
    private String IncotermsLocation2;

    @JsonProperty("IncotermsVersion")
    private String IncotermsVersion;

    @JsonProperty("IncotermsLocation1")
    private String IncotermsLocation1;

    @JsonProperty("IncotermsSupChnLoc1AddlUUID")
    private String IncotermsSupChnLoc1AddlUUID;

    @JsonProperty("IncotermsSupChnLoc2AddlUUID")
    private String IncotermsSupChnLoc2AddlUUID;

    @JsonProperty("IncotermsSupChnDvtgLocAddlUUID")
    private String IncotermsSupChnDvtgLocAddlUUID;

    @JsonProperty("DeletionIndicator")
    private boolean DeletionIndicator;

    @JsonProperty("IncotermsTransferLocation")
    private String IncotermsTransferLocation;

    @JsonProperty("InspSbstHasNoTimeOrQuantity")
    private boolean InspSbstHasNoTimeOrQuantity;

    @JsonProperty("InvoiceDate")
    private String InvoiceDate;

    @JsonProperty("ItemOrderProbabilityInPercent")
    private String ItemOrderProbabilityInPercent;

    @JsonProperty("ManualInvoiceMaintIsRelevant")
    private boolean ManualInvoiceMaintIsRelevant;

    @JsonProperty("MaxNmbrOfPartialDelivery")
    private String MaxNmbrOfPartialDelivery;

    @JsonProperty("OrderCombinationIsAllowed")
    private boolean OrderCombinationIsAllowed;

    @JsonProperty("OrderIsBlockedForCustomer")
    private String OrderIsBlockedForCustomer;

    @JsonProperty("OverdelivTolrtdLmtRatioInPct")
    private String OverdelivTolrtdLmtRatioInPct;

    @JsonProperty("PartialDeliveryIsAllowed")
    private String PartialDeliveryIsAllowed;

    @JsonProperty("PriceListType")
    private String PriceListType;

    @JsonProperty("ProductUnitGroup")
    private String ProductUnitGroup;

    @JsonProperty("ProofOfDeliveryTimeValue")
    private String ProofOfDeliveryTimeValue;

    @JsonProperty("SalesGroup")
    private String SalesGroup;

    @JsonProperty("SalesItemProposal")
    private String SalesItemProposal;

    @JsonProperty("SalesOffice")
    private String SalesOffice;

    @JsonProperty("ShippingCondition")
    private String ShippingCondition;

    @JsonProperty("SlsDocIsRlvtForProofOfDeliv")
    private boolean SlsDocIsRlvtForProofOfDeliv;

    @JsonProperty("SlsUnlmtdOvrdelivIsAllwd")
    private boolean SlsUnlmtdOvrdelivIsAllwd;

    @JsonProperty("SupplyingPlant")
    private String SupplyingPlant;

    @JsonProperty("SalesDistrict")
    private String SalesDistrict;

    @JsonProperty("UnderdelivTolrtdLmtRatioInPct")
    private String UnderdelivTolrtdLmtRatioInPct;

    @JsonProperty("InvoiceListSchedule")
    private String InvoiceListSchedule;

    @JsonProperty("ExchangeRateType")
    private String ExchangeRateType;

    @JsonProperty("AdditionalCustomerGroup1")
    private String AdditionalCustomerGroup1;

    @JsonProperty("AdditionalCustomerGroup2")
    private String AdditionalCustomerGroup2;

    @JsonProperty("AdditionalCustomerGroup3")
    private String AdditionalCustomerGroup3;

    @JsonProperty("AdditionalCustomerGroup4")
    private String AdditionalCustomerGroup4;

    @JsonProperty("AdditionalCustomerGroup5")
    private String AdditionalCustomerGroup5;

    @JsonProperty("PaymentGuaranteeProcedure")
    private String PaymentGuaranteeProcedure;

    @JsonProperty("CustomerAccountGroup")
    private String CustomerAccountGroup;

    @JsonProperty("to_PartnerFunction")
    private PartnerFunctionWrapper toPartnerFunction;
}
