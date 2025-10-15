package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessPartnerResults {
    private List<BusinessPartnerDTO> results;
    public List<BusinessPartnerDTO> getResults() { return results; }
    public void setResults(List<BusinessPartnerDTO> results) { this.results = results; }
}
