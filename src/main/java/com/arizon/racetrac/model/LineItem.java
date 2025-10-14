package com.arizon.racetrac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineItem {

    @JsonProperty("orderLinePriority")
    private String orderLinePriority;

    @JsonProperty("isSaturdayDeliveryAllowed")
    private Boolean isSaturdayDeliveryAllowed;

    @JsonProperty("shipmentSplitMethod")
    private String shipmentSplitMethod;

    @JsonProperty("lineItemDetail")
    private List<LineItemDetail> lineItemDetail;

    @JsonProperty("transactionalTradeItem")
    private TransactionalTradeItem transactionalTradeItem;

    @JsonProperty("requestedQuantity")
    private Quantity requestedQuantity;

    @JsonProperty("lineItemNumber")
    private int lineItemNumber;

    @JsonProperty("isCaseSplittingAllowed")
    private Boolean isCaseSplittingAllowed;
}

