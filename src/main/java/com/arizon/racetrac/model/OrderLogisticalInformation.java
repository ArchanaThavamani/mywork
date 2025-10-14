package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderLogisticalInformation {

    @JsonProperty("shipFrom")
    private Party shipFrom;

    @JsonProperty("orderLogisticalDateInformation")
    private OrderLogisticalDateInformation orderLogisticalDateInformation;

    @JsonProperty("shipTo")
    private Party shipTo;
}
