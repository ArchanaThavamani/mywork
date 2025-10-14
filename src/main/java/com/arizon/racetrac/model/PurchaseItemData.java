package com.arizon.racetrac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

// Root DTO

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseItemData {
    private PurchaseItem purchaseItem;
    private String itemName;
    private String itemDescription;
    private String itemIdentifier;
    private List<PurchaseItemGroup> purchaseItemGroup;
    private BaseUnitOfMeasure baseUnitOfMeasure;
    private boolean preferred;

    private PurchaseItemStatus purchaseItemStatus;
    private SpendCategoryForItem spendCategoryForItem;
    private UnitPrice unitPrice;
    private String lastUpdateTimestamp; 
    @JsonProperty("alternateItemIdentifiersForItem")
    private List<AlternateItemIdentifier> alternateItemIdentifiersForItem;
    
}

