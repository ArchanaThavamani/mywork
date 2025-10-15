package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SAPProduct {
    @JsonProperty("Product")
    private String product;

    @JsonProperty("ProductType")
    private String productType;

    @JsonProperty("CrossPlantStatus")
    private String crossPlantStatus;

    @JsonProperty("CreationDate")
    private String creationDate;

    @JsonProperty("CreatedByUser")
    private String createdByUser;

    @JsonProperty("LastChangeDate")
    private String lastChangeDate;

    @JsonProperty("LastChangedByUser")
    private String lastChangedByUser;

    @JsonProperty("LastChangeDateTime")
    private String lastChangeDateTime;

    @JsonProperty("IsMarkedForDeletion")
    private boolean isMarkedForDeletion;

    @JsonProperty("ProductOldID")
    private String productOldId;

    @JsonProperty("GrossWeight")
    private String grossWeight;

    @JsonProperty("WeightUnit")
    private String weightUnit;

    @JsonProperty("NetWeight")
    private String netWeight;

    @JsonProperty("ProductGroup")
    private String productGroup;

    @JsonProperty("BaseUnit")
    private String baseUnit;

    @JsonProperty("ItemCategoryGroup")
    private String itemCategoryGroup;

    @JsonProperty("Division")
    private String division;

    @JsonProperty("IndustryStandardName")
    private String industryStandardName;

    @JsonProperty("QltyMgmtInProcmtIsActive")
    private boolean qltyMgmtInProcmtIsActive;

    @JsonProperty("IndustrySector")
    private String industrySector;

    @JsonProperty("to_Description")
    private ProductDescriptionWrapper toDescription;

    @JsonProperty("to_Plant")
    private ProductPlantWrapper toPlant;
    
    @JsonProperty("to_SalesDelivery")
    private ProductSalesDeliveryWrapper toSalesDelivery;
}