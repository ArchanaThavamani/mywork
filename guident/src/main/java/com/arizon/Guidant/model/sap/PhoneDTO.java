package com.arizon.Guidant.model.sap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneDTO {
    @JsonProperty("AddressID")
    private String AddressID;

    @JsonProperty("Person")
    private String Person;

    @JsonProperty("OrdinalNumber")
    private String OrdinalNumber;

    @JsonProperty("DestinationLocationCountry")
    private String DestinationLocationCountry;

    @JsonProperty("IsDefaultPhoneNumber")
    private boolean IsDefaultPhoneNumber;

    @JsonProperty("PhoneNumber")
    private String PhoneNumber;

    @JsonProperty("PhoneNumberExtension")
    private String PhoneNumberExtension;

    @JsonProperty("InternationalPhoneNumber")
    private String InternationalPhoneNumber;

    @JsonProperty("PhoneNumberType")
    private String PhoneNumberType;

    @JsonProperty("AddressCommunicationRemarkText")
    private String AddressCommunicationRemarkText;
}
