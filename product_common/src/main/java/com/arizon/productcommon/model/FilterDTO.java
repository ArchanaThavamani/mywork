package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterDTO {
    private String id;

    @JsonProperty("display_name")
    private String displayName;

    private String type;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("items_to_show")
    private Integer itemsToShow;

    @JsonProperty("collapsed_by_default")
    private Boolean collapsedByDefault;

    @JsonProperty("display_product_count")
    private Boolean displayProductCount;

    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @JsonProperty("facet_id") 
    private Integer facetId;

    private String facet;
}
