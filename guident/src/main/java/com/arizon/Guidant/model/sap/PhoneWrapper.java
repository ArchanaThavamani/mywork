package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneWrapper {
    private List<PhoneDTO> results;
    public List<PhoneDTO> getResults() { return results; }
    public void setResults(List<PhoneDTO> results) { this.results = results; }
}
