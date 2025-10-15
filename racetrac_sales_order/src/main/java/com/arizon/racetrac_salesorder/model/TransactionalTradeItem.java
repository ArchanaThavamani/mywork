package com.arizon.racetrac_salesorder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.ToString;
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Data
public class TransactionalTradeItem {
	private String primaryId;
}
