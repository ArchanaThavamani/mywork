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
public class ProductDescription {
    @JsonProperty("Product")
    private String product;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("ProductDescription")
    private String productDescription;
}
