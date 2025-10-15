package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessPartnerDTO {
    @JsonProperty("BusinessPartner")
    private String BusinessPartner;

    @JsonProperty("Customer")
    private String Customer;

    @JsonProperty("Supplier")
    private String Supplier;

    @JsonProperty("AcademicTitle")
    private String AcademicTitle;

    @JsonProperty("AuthorizationGroup")
    private String AuthorizationGroup;

    @JsonProperty("BusinessPartnerCategory")
    private String BusinessPartnerCategory;

    @JsonProperty("BusinessPartnerFullName")
    private String BusinessPartnerFullName;

    @JsonProperty("BusinessPartnerGrouping")
    private String BusinessPartnerGrouping;

    @JsonProperty("BusinessPartnerName")
    private String BusinessPartnerName;

    @JsonProperty("BusinessPartnerUUID")
    private String BusinessPartnerUUID;

    @JsonProperty("CorrespondenceLanguage")
    private String CorrespondenceLanguage;

    @JsonProperty("CreatedByUser")
    private String CreatedByUser;

    @JsonProperty("CreationDate")
    private String CreationDate;

    @JsonProperty("CreationTime")
    private String CreationTime;

    @JsonProperty("FirstName")
    private String FirstName;

    @JsonProperty("FormOfAddress")
    private String FormOfAddress;

    @JsonProperty("Industry")
    private String Industry;

    @JsonProperty("InternationalLocationNumber1")
    private String InternationalLocationNumber1;

    @JsonProperty("InternationalLocationNumber2")
    private String InternationalLocationNumber2;

    @JsonProperty("IsFemale")
    private boolean IsFemale;

    @JsonProperty("IsMale")
    private boolean IsMale;

    @JsonProperty("IsNaturalPerson")
    private String IsNaturalPerson;

    @JsonProperty("IsSexUnknown")
    private boolean IsSexUnknown;

    @JsonProperty("GenderCodeName")
    private String GenderCodeName;

    @JsonProperty("Language")
    private String Language;

    @JsonProperty("LastChangeDate")
    private String LastChangeDate;

    @JsonProperty("LastChangeTime")
    private String LastChangeTime;

    @JsonProperty("LastChangedByUser")
    private String LastChangedByUser;

    @JsonProperty("LastName")
    private String LastName;

    @JsonProperty("LegalForm")
    private String LegalForm;

    @JsonProperty("OrganizationBPName1")
    private String OrganizationBPName1;

    @JsonProperty("OrganizationBPName2")
    private String OrganizationBPName2;

    @JsonProperty("OrganizationBPName3")
    private String OrganizationBPName3;

    @JsonProperty("OrganizationBPName4")
    private String OrganizationBPName4;

    @JsonProperty("OrganizationFoundationDate")
    private String OrganizationFoundationDate;

    @JsonProperty("OrganizationLiquidationDate")
    private String OrganizationLiquidationDate;

    @JsonProperty("SearchTerm1")
    private String SearchTerm1;

    @JsonProperty("SearchTerm2")
    private String SearchTerm2;

    @JsonProperty("AdditionalLastName")
    private String AdditionalLastName;

    @JsonProperty("BirthDate")
    private String BirthDate;

    @JsonProperty("BusinessPartnerBirthDateStatus")
    private String BusinessPartnerBirthDateStatus;

    @JsonProperty("BusinessPartnerBirthplaceName")
    private String BusinessPartnerBirthplaceName;

    @JsonProperty("BusinessPartnerDeathDate")
    private String BusinessPartnerDeathDate;

    @JsonProperty("BusinessPartnerIsBlocked")
    private boolean BusinessPartnerIsBlocked;

    @JsonProperty("BusinessPartnerType")
    private String BusinessPartnerType;

    @JsonProperty("ETag")
    private String ETag;

    @JsonProperty("GroupBusinessPartnerName1")
    private String GroupBusinessPartnerName1;

    @JsonProperty("GroupBusinessPartnerName2")
    private String GroupBusinessPartnerName2;

    @JsonProperty("IndependentAddressID")
    private String IndependentAddressID;

    @JsonProperty("InternationalLocationNumber3")
    private String InternationalLocationNumber3;

    @JsonProperty("MiddleName")
    private String MiddleName;

    @JsonProperty("NameCountry")
    private String NameCountry;

    @JsonProperty("NameFormat")
    private String NameFormat;

    @JsonProperty("PersonFullName")
    private String PersonFullName;

    @JsonProperty("PersonNumber")
    private String PersonNumber;

    @JsonProperty("IsMarkedForArchiving")
    private boolean IsMarkedForArchiving;

    @JsonProperty("BusinessPartnerIDByExtSystem")
    private String BusinessPartnerIDByExtSystem;

    @JsonProperty("BusinessPartnerPrintFormat")
    private String BusinessPartnerPrintFormat;

    @JsonProperty("BusinessPartnerOccupation")
    private String BusinessPartnerOccupation;

    @JsonProperty("BusPartMaritalStatus")
    private String BusPartMaritalStatus;

    @JsonProperty("BusPartNationality")
    private String BusPartNationality;

    @JsonProperty("BusinessPartnerBirthName")
    private String BusinessPartnerBirthName;

    @JsonProperty("BusinessPartnerSupplementName")
    private String BusinessPartnerSupplementName;

    @JsonProperty("NaturalPersonEmployerName")
    private String NaturalPersonEmployerName;

    @JsonProperty("LastNamePrefix")
    private String LastNamePrefix;

    @JsonProperty("LastNameSecondPrefix")
    private String LastNameSecondPrefix;

    @JsonProperty("Initials")
    private String Initials;

    @JsonProperty("BPDataControllerIsNotRequired")
    private boolean BPDataControllerIsNotRequired;

    @JsonProperty("TradingPartner")
    private String TradingPartner;

    @JsonProperty("to_BusinessPartnerAddress")
    private AddressWrapper toBusinessPartnerAddress;

    @JsonProperty("to_Customer")
    private CustomerDTO toCustomer;
}
