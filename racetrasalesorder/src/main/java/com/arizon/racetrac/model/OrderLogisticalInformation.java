package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.arizon.racetrac_salesorder.model.*;


import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString

@Data
public class OrderLogisticalInformation {
	    private RouteVia shipFrom;
	    private OrderLogisticalDateInformation orderLogisticalDateInformation;
	    private RouteVia routeVia;
	    private ShipmentTransportationInformation shipmentTransportationInformation;
	    private RouteVia shipTo;
}
