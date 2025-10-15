package com.arizon.Guidant.model.sap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerFunctionDTO {
    @JsonProperty("Customer")
    private String Customer;

    @JsonProperty("SalesOrganization")
    private String SalesOrganization;

    @JsonProperty("DistributionChannel")
    private String DistributionChannel;

    @JsonProperty("Division")
    private String Division;

    @JsonProperty("PartnerCounter")
    private String PartnerCounter;

    @JsonProperty("PartnerFunction")
    private String PartnerFunction;

    @JsonProperty("BPCustomerNumber")
    private String BPCustomerNumber;

    @JsonProperty("CustomerPartnerDescription")
    private String CustomerPartnerDescription;

    @JsonProperty("DefaultPartner")
    private boolean DefaultPartner;

    @JsonProperty("Supplier")
    private String Supplier;

    @JsonProperty("PersonnelNumber")
    private String PersonnelNumber;

    @JsonProperty("ContactPerson")
    private String ContactPerson;

    @JsonProperty("AddressID")
    private String AddressID;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;
}
