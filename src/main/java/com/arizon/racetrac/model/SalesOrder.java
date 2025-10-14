package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// Root DTO 

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesOrder {

    @JsonProperty("orderSubType")
    private String orderSubType;

    @JsonProperty("orderLogisticalInformation")
    private OrderLogisticalInformation orderLogisticalInformation;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("documentStatusCode")
    private String documentStatusCode;

    @JsonProperty("entityId")
    private String entityId;

    @JsonProperty("documentActionCode")
    private String documentActionCode;

    @JsonProperty("buyer")
    private Party buyer;

    @JsonProperty("tmCustomerCode")
    private String tmCustomerCode;

    @JsonProperty("lineItem")
    private List<LineItem> lineItem;

}

