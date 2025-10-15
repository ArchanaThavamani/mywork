package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSalesAreaWrapper {
    private List<CustomerSalesAreaDTO> results;
    public List<CustomerSalesAreaDTO> getResults() { return results; }
    public void setResults(List<CustomerSalesAreaDTO> results) { this.results = results; }
}
