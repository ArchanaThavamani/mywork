package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDTO {
     @JsonProperty("AddressID")
    private String AddressID;

    @JsonProperty("ValidityStartDate")
    private String ValidityStartDate;

    @JsonProperty("ValidityEndDate")
    private String ValidityEndDate;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;

    @JsonProperty("AddressUUID")
    private String AddressUUID;

    @JsonProperty("AdditionalStreetPrefixName")
    private String AdditionalStreetPrefixName;

    @JsonProperty("AdditionalStreetSuffixName")
    private String AdditionalStreetSuffixName;

    @JsonProperty("AddressTimeZone")
    private String AddressTimeZone;

    @JsonProperty("CareOfName")
    private String CareOfName;

    @JsonProperty("CityCode")
    private String CityCode;

    @JsonProperty("CityName")
    private String CityName;

    @JsonProperty("CompanyPostalCode")
    private String CompanyPostalCode;

    @JsonProperty("Country")
    private String Country;

    @JsonProperty("County")
    private String County;

    @JsonProperty("DeliveryServiceNumber")
    private String DeliveryServiceNumber;

    @JsonProperty("DeliveryServiceTypeCode")
    private String DeliveryServiceTypeCode;

    @JsonProperty("District")
    private String District;

    @JsonProperty("FormOfAddress")
    private String FormOfAddress;

    @JsonProperty("FullName")
    private String FullName;

    @JsonProperty("HomeCityName")
    private String HomeCityName;

    @JsonProperty("HouseNumber")
    private String HouseNumber;

    @JsonProperty("HouseNumberSupplementText")
    private String HouseNumberSupplementText;

    @JsonProperty("Language")
    private String Language;

    @JsonProperty("POBox")
    private String POBox;

    @JsonProperty("POBoxDeviatingCityName")
    private String POBoxDeviatingCityName;

    @JsonProperty("POBoxDeviatingCountry")
    private String POBoxDeviatingCountry;

    @JsonProperty("POBoxDeviatingRegion")
    private String POBoxDeviatingRegion;

    @JsonProperty("POBoxIsWithoutNumber")
    private boolean POBoxIsWithoutNumber;

    @JsonProperty("POBoxLobbyName")
    private String POBoxLobbyName;

    @JsonProperty("POBoxPostalCode")
    private String POBoxPostalCode;

    @JsonProperty("Person")
    private String Person;

    @JsonProperty("PostalCode")
    private String PostalCode;

    @JsonProperty("PrfrdCommMediumType")
    private String PrfrdCommMediumType;

    @JsonProperty("Region")
    private String Region;

    @JsonProperty("StreetName")
    private String StreetName;

    @JsonProperty("StreetPrefixName")
    private String StreetPrefixName;

    @JsonProperty("StreetSuffixName")
    private String StreetSuffixName;

    @JsonProperty("TaxJurisdiction")
    private String TaxJurisdiction;

    @JsonProperty("TransportZone")
    private String TransportZone;

    @JsonProperty("AddressIDByExternalSystem")
    private String AddressIDByExternalSystem;

    @JsonProperty("CountyCode")
    private String CountyCode;

    @JsonProperty("TownshipCode")
    private String TownshipCode;

    @JsonProperty("TownshipName")
    private String TownshipName;

    @JsonProperty("to_EmailAddress")
    private EmailWrapper toEmailAddress;

    @JsonProperty("to_PhoneNumber")
    private PhoneWrapper toPhoneNumber;
}
