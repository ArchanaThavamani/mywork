package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PriceListConditionValidityDTO {

	 @JsonProperty("ConditionRecord")
	    private String conditionRecord;

	 
	 @JsonProperty("CustomerPriceGroup")
	    private String customerPriceGroup;

	 
	    @JsonProperty("ConditionValidityStartDate")
	    private String conditionValidityStartDate;

	    @JsonProperty("ConditionValidityEndDate")
	    private String conditionValidityEndDate;

	    @JsonProperty("ConditionApplication")
	    private String conditionApplication;

	    @JsonProperty("ConditionType")
	    private String conditionType;

	    @JsonProperty("SalesOrganization")
	    private String salesOrganization;

	    @JsonProperty("Material")
	    private String material;
	    
	    @JsonProperty("MaterialPricingGroup")
	    private String materialPricingGroup;
}
