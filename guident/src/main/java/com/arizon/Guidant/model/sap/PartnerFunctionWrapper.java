package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerFunctionWrapper {
    private List<PartnerFunctionDTO> results;
    public List<PartnerFunctionDTO> getResults() { return results; }
    public void setResults(List<PartnerFunctionDTO> results) { this.results = results; }
}
