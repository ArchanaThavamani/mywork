package com.arizon.Guidant.model.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListConditionDTO {
	@JsonProperty("ConditionRateValue")
    private String conditionRateValue;

    @JsonProperty("ConditionRateValueUnit")
    private String conditionRateValueUnit;

    @JsonProperty("ConditionAlternativeCurrency")
    private String conditionAlternativeCurrency;
    
    

    @JsonProperty("to_SlsPrcgCndnRecdValidity")
    private ValidityWrapper validityWrapper;

    @Data
    public static class ValidityWrapper {
        @JsonProperty("results")
        private List<PriceListConditionValidityDTO> results;
    }


}
