package com.arizon.racetrac_salesorder.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.arizon.racetrac_salesorder.model.*;

import lombok.Data;
import lombok.ToString;
@JsonIgnoreProperties(ignoreUnknown = true)

@ToString
@Data
public class OrderRelease {
	private String cashOnDeliveryType;
    private String orderSubType;
    private String orderTypeCode;
    private boolean isSignatureRequired;
    private OrderLogisticalInformation orderLogisticalInformation;
    private String cashOnDeliveryPaymentType;
    private String orderId;
    private String documentStatusCode;
    private List<OrderNote> orderNote;
    private boolean isReleaseRemainingLinesEnabled;
    private String referenceOrderNumber;
    private String freightTermsCode;
    private FreightRate freightRate;
    private String changeReasonCode;
    private String dutyPaymentAccount;
    private boolean requiresTransportPlanning;
    private String ownerOfOrder;
    private boolean isCustomsClearanceExpedited;
    private RouteVia billTo;
    private String customerDepartmentId;
    private String defaultOrderLineChangeReason;
    private String creationDateTime;
    private boolean isRushOrder;
    private boolean isFreightBilled;
    private ReturnMaterialsAuthorizationNumber returnMaterialsAuthorizationNumber;
    private String entityId;
    private String documentActionCode;
    private String combineOrdersBy;
    private boolean isConsigneeBilled;
    private boolean isCarrierChangeEnabled;
    private List<LineItem> lineItem;
    private String customerDestinationId;
    private String purchaseOrderType;
    private List<String> orderConsolidationGroup;
    private Meta meta;
    private String originalOrderTypeCode;
    private boolean isOrderWaveable;
    private PurchaseOrder purchaseOrder;
    private String plannedOrderSequence;
    private String dutyPaymentResponsibleParty;
    private boolean isUrgentShipment;

}
