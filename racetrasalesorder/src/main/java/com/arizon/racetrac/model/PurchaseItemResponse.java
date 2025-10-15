package com.arizon.racetrac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseItemResponse {
    private int total;
    private List<PurchaseItemData> data;
}
