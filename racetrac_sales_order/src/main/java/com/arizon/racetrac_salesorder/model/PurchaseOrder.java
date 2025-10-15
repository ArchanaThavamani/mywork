package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class PurchaseOrder {
	 private String lineItemIdentifier;
	    private String entityId;
	    private String subLineId;
	    private String creationDateTime;
}
