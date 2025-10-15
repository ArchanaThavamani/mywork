package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ShipmentTransportationInformation {
	 private String transportServiceCategoryType;
	    private RouteVia carrier;
	    private String transportServiceLevelCode;
}
