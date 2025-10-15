package com.arizon.Guidant.model.sap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesOrderItem {

    @JsonProperty("SalesOrderItem")
    private String salesOrderItem;

    @JsonProperty("HigherLevelItem")
    private String higherLevelItem;

    @JsonProperty("BillingPlan")
    private String billingPlan;

    @JsonProperty("RequestedQuantity")
    private String requestedQuantity;

    @JsonProperty("RequestedQuantityUnit")
    private String requestedQuantityUnit;

    @JsonProperty("NetAmount")
    private String netAmount;

    @JsonProperty("Material")
    private String material;

    @JsonProperty("TransactionCurrency")
    private String transactionCurrency;

    @JsonProperty("ItemGrossWeight")
    private String itemGrossWeight;

    @JsonProperty("ItemNetWeight")
    private String itemNetWeight;

    @JsonProperty("ItemWeightUnit")
    private String itemWeightUnit;

    @JsonProperty("BillingDocumentDate")
    private String billingDocumentDate;

    @JsonProperty("TaxAmount")
    private String taxAmount;

    @JsonProperty("ProductTaxClassification1")
    private String productTaxClassification1;

    @JsonProperty("CustomerPaymentTerms")
    private String customerPaymentTerms;

    @JsonProperty("DeliveryStatus")
    private String deliveryStatus;

    @JsonProperty("SDProcessStatus")
    private String sdProcessStatus;

    @JsonProperty("Subtotal1Amount")
    private String subtotal1Amount;

    @JsonProperty("Subtotal2Amount")
    private String subtotal2Amount;

    @JsonProperty("Subtotal3Amount")
    private String subtotal3Amount;

    @JsonProperty("Subtotal4Amount")
    private String subtotal4Amount;

    @JsonProperty("Subtotal5Amount")
    private String subtotal5Amount;

    @JsonProperty("Subtotal6Amount")
    private String subtotal6Amount;

    @JsonProperty("to_PricingElement")
    private PricingElementWrapper pricingElementWrapper;
    
    @JsonProperty("to_SalesDelivery")
    private ProductSalesDeliveryWrapper salesDeliveryWrapper;
}