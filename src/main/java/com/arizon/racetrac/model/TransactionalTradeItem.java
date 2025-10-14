package com.arizon.racetrac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionalTradeItem {

    @JsonProperty("additionalTradeItemId")
    private List<TradeItemId> additionalTradeItemId;

    @JsonProperty("primaryId")
    private String primaryId;
}

