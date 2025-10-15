package com.arizon.Guidant.model.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesOrderPartnerWrapper {
	
     @JsonProperty("results")
	 private List<SalesOrderPartner> results;
}
