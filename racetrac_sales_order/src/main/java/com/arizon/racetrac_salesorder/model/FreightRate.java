package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FreightRate {
	 private String currencyCode;
	    private double value;

}
