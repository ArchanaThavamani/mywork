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
public class ProductSalesDelivery {
    @JsonProperty("Product")
    private String product;

    @JsonProperty("ProductSalesOrg")
    private String productSalesOrg;

    @JsonProperty("ProductDistributionChnl")
    private String productDistributionChnl;

    @JsonProperty("MinimumOrderQuantity")
    private String minimumOrderQuantity;

    @JsonProperty("SupplyingPlant")
    private String supplyingPlant;
    
    @JsonProperty("PriceSpecificationProductGroup")
    private String priceSpecificationProductGroup;

    @JsonProperty("ItemCategoryGroup")
    private String itemCategoryGroup;

}
