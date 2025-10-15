package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressWrapper {
    private List<AddressDTO> results;
    public List<AddressDTO> getResults() { return results; }
    public void setResults(List<AddressDTO> results) { this.results = results; }
}
