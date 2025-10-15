package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class SalesOrder {
	private String entityId;
    private int lineItemNumber;

}
