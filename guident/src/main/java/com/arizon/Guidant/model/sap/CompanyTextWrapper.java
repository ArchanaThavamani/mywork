package com.arizon.Guidant.model.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyTextWrapper {
    @JsonProperty("results")
    private List<CompanyTextDTO> results;
}