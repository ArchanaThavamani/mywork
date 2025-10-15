package com.arizon.Guidant.model.sap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDTO {
     @JsonProperty("AddressID")
    private String AddressID;

    @JsonProperty("Person")
    private String Person;

    @JsonProperty("OrdinalNumber")
    private String OrdinalNumber;

    @JsonProperty("IsDefaultEmailAddress")
    private boolean IsDefaultEmailAddress;

    @JsonProperty("EmailAddress")
    private String EmailAddress;

    @JsonProperty("SearchEmailAddress")
    private String SearchEmailAddress;

    @JsonProperty("AddressCommunicationRemarkText")
    private String AddressCommunicationRemarkText;
}
