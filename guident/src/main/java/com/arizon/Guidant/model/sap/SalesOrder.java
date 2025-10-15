package com.arizon.Guidant.model.sap;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesOrder {

    @JsonProperty("SalesOrderType")
    private String salesOrderType;

    @JsonProperty("SalesOrderTypeInternalCode")
    private String salesOrderTypeInternalCode;

    @JsonProperty("SalesOrganization")
    private String salesOrganization;

    @JsonProperty("DistributionChannel")
    private String distributionChannel;

    @JsonProperty("OrganizationDivision")
    private String organizationDivision;

    @JsonProperty("SalesDistrict")
    private String salesDistrict;

    @JsonProperty("SoldToParty")
    private String soldToParty;
    
    @JsonProperty("ShipToParty")
    private String shipToParty;

    @JsonProperty("CreationDate")
    private String creationDate;

    @JsonProperty("CreatedByUser")
    private String createdByUser;

    @JsonProperty("LastChangeDate")
    private String lastChangeDate;

    @JsonProperty("SenderBusinessSystemName")
    private String senderBusinessSystemName;

    @JsonProperty("ExternalDocumentID")
    private String externalDocumentID;

    @JsonProperty("LastChangeDateTime")
    private String lastChangeDateTime;

    @JsonProperty("ExternalDocLastChangeDateTime")
    private String externalDocLastChangeDateTime;

    @JsonProperty("PurchaseOrderByCustomer")
    private String purchaseOrderByCustomer;

    @JsonProperty("PurchaseOrderByShipToParty")
    private String purchaseOrderByShipToParty;

    @JsonProperty("CustomerPurchaseOrderType")
    private String customerPurchaseOrderType;

    @JsonProperty("CustomerPurchaseOrderDate")
    private String customerPurchaseOrderDate;

    @JsonProperty("SalesOrderDate")
    private String salesOrderDate;

    @JsonProperty("TotalNetAmount")
    private String totalNetAmount;

    @JsonProperty("OverallDeliveryStatus")
    private String overallDeliveryStatus;

    @JsonProperty("TotalBlockStatus")
    private String totalBlockStatus;

    @JsonProperty("OverallOrdReltdBillgStatus")
    private String overallOrdReltdBillgStatus;

    @JsonProperty("OverallSDDocReferenceStatus")
    private String overallSDDocReferenceStatus;

    @JsonProperty("TransactionCurrency")
    private String transactionCurrency;

    @JsonProperty("SDDocumentReason")
    private String sdDocumentReason;

    @JsonProperty("PricingDate")
    private String pricingDate;

    @JsonProperty("PriceDetnExchangeRate")
    private String priceDetnExchangeRate;

    @JsonProperty("BillingPlan")
    private String billingPlan;

    @JsonProperty("RequestedDeliveryDate")
    private String requestedDeliveryDate;

    @JsonProperty("ShippingCondition")
    private String shippingCondition;

    @JsonProperty("CompleteDeliveryIsDefined")
    private boolean completeDeliveryIsDefined;

    @JsonProperty("ShippingType")
    private String shippingType;

    @JsonProperty("HeaderBillingBlockReason")
    private String headerBillingBlockReason;

    @JsonProperty("DeliveryBlockReason")
    private String deliveryBlockReason;

    @JsonProperty("DeliveryDateTypeRule")
    private String deliveryDateTypeRule;

    @JsonProperty("IncotermsClassification")
    private String incotermsClassification;

    @JsonProperty("IncotermsTransferLocation")
    private String incotermsTransferLocation;

    @JsonProperty("IncotermsLocation1")
    private String incotermsLocation1;

    @JsonProperty("IncotermsLocation2")
    private String incotermsLocation2;

    @JsonProperty("IncotermsVersion")
    private String incotermsVersion;

    @JsonProperty("CustomerPriceGroup")
    private String customerPriceGroup;

    @JsonProperty("PriceListType")
    private String priceListType;

    @JsonProperty("CustomerPaymentTerms")
    private String customerPaymentTerms;

    @JsonProperty("PaymentMethod")
    private String paymentMethod;

    @JsonProperty("FixedValueDate")
    private String fixedValueDate;

    @JsonProperty("AssignmentReference")
    private String assignmentReference;

    @JsonProperty("ReferenceSDDocument")
    private String referenceSDDocument;

    @JsonProperty("ReferenceSDDocumentCategory")
    private String referenceSDDocumentCategory;

    @JsonProperty("AccountingDocExternalReference")
    private String accountingDocExternalReference;

    @JsonProperty("CustomerAccountAssignmentGroup")
    private String customerAccountAssignmentGroup;

    @JsonProperty("AccountingExchangeRate")
    private String accountingExchangeRate;

    @JsonProperty("CustomerGroup")
    private String customerGroup;

    @JsonProperty("AdditionalCustomerGroup1")
    private String additionalCustomerGroup1;

    @JsonProperty("AdditionalCustomerGroup2")
    private String additionalCustomerGroup2;

    @JsonProperty("AdditionalCustomerGroup3")
    private String additionalCustomerGroup3;

    @JsonProperty("AdditionalCustomerGroup4")
    private String additionalCustomerGroup4;

    @JsonProperty("AdditionalCustomerGroup5")
    private String additionalCustomerGroup5;

    @JsonProperty("SlsDocIsRlvtForProofOfDeliv")
    private boolean slsDocIsRlvtForProofOfDeliv;

    @JsonProperty("CustomerTaxClassification1")
    private String customerTaxClassification1;

    @JsonProperty("CustomerTaxClassification2")
    private String customerTaxClassification2;

    @JsonProperty("CustomerTaxClassification3")
    private String customerTaxClassification3;

    @JsonProperty("CustomerTaxClassification4")
    private String customerTaxClassification4;

    @JsonProperty("CustomerTaxClassification5")
    private String customerTaxClassification5;

    @JsonProperty("CustomerTaxClassification6")
    private String customerTaxClassification6;

    @JsonProperty("CustomerTaxClassification7")
    private String customerTaxClassification7;

    @JsonProperty("CustomerTaxClassification8")
    private String customerTaxClassification8;

    @JsonProperty("CustomerTaxClassification9")
    private String customerTaxClassification9;

    @JsonProperty("TaxDepartureCountry")
    private String taxDepartureCountry;

    @JsonProperty("VATRegistrationCountry")
    private String vatRegistrationCountry;

    @JsonProperty("SalesOrderApprovalReason")
    private String salesOrderApprovalReason;

    @JsonProperty("SalesDocApprovalStatus")
    private String salesDocApprovalStatus;

    @JsonProperty("OverallSDProcessStatus")
    private String overallSDProcessStatus;

    @JsonProperty("TotalCreditCheckStatus")
    private String totalCreditCheckStatus;

    @JsonProperty("OverallTotalDeliveryStatus")
    private String overallTotalDeliveryStatus;

    @JsonProperty("to_Item")
    private SalesOrderItemWrapper toItem;
    
    @JsonProperty("to_Partner")
    private SalesOrderPartnerWrapper toPartner;

}
