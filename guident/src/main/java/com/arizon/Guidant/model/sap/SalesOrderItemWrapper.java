package com.arizon.Guidant.model.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesOrderItemWrapper {
    @JsonProperty("results")
    private List<SalesOrderItem> results;
}