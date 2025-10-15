package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderLogisticalDateInformation {
	    private RequestedDeliveryDateRange requestedDeliveryDateRange;
	    private RequestedDeliveryDateRange requestedShipDateRange;
}
