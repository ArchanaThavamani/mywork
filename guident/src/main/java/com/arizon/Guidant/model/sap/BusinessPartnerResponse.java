package com.arizon.Guidant.model.sap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessPartnerResponse {
    private BusinessPartnerResults d;
    public BusinessPartnerResults getD() { return d; }
    public void setD(BusinessPartnerResults d) { this.d = d; }    
}
