package com.arizon.racetrac_salesorder.model;

import java.util.List;
import com.arizon.racetrac_salesorder.model.*;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {
	private boolean isSaturdayDeliveryAllowed;
    private String inventoryStatusProgression;
    private List<LineItemDetail> lineItemDetail;
    private MinimumShelfLife minimumShelfLife;
    private TransactionalTradeItem transactionalTradeItem;
    private boolean isOrderLineAllocatable;
    private String referenceOrderNumber;
    private SalesOrder salesOrder;
    private String freightTermsCode;
    private List<OrderNote> orderLineNote;
    private String customerAccountId;
    private RouteVia ownerOfTradeItem;
    private String itemStatus;
    private RequestedQuantity requestedQuantity;
    private String subLineId;
    private String customerDepartmentId;
    private int lineItemNumber;
    private String customerProjectId;
    private boolean isCaseSplittingAllowed;

}
