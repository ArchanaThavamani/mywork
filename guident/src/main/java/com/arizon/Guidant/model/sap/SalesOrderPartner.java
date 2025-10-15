package com.arizon.Guidant.model.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesOrderPartner {
    
	@JsonProperty("PartnerFunction")
	private String partnerFunction;
	
	@JsonProperty("Customer")
	private String customer;
}
