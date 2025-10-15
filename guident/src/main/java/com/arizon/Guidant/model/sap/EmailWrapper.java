package com.arizon.Guidant.model.sap;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailWrapper {
    private List<EmailDTO> results;
    public List<EmailDTO> getResults() { return results; }
    public void setResults(List<EmailDTO> results) { this.results = results; }
}
