package com.arizon.Guidant.model.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyTextDTO {
    @JsonProperty("CompanyText")
    private String companyText;
}