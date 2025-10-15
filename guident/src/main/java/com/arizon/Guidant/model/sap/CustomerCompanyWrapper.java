package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerCompanyWrapper {
    private List<CustomerCompanyDTO> results;
    public List<CustomerCompanyDTO> getResults() { return results; }
    public void setResults(List<CustomerCompanyDTO> results) { this.results = results; }
}
