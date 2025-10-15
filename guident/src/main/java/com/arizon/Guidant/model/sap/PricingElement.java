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
public class PricingElement {
    @JsonProperty("ConditionType")
    private String conditionType;

    @JsonProperty("PricingDateTime")
    private String pricingDateTime;

    @JsonProperty("ConditionCalculationType")
    private String conditionCalculationType;

    @JsonProperty("ConditionQuantity")
    private String conditionQuantity;

    @JsonProperty("ConditionRateValue")
    private String conditionRateValue;

    @JsonProperty("ConditionQuantityUnit")
    private String conditionQuantityUnit;
}


