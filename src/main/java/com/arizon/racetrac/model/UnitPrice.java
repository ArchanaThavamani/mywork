package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitPrice {
    private String value;
    private String currency;
}
