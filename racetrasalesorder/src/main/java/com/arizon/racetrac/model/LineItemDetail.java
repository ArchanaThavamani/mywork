package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.arizon.racetrac_salesorder.model.*;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItemDetail {
	 private OrderLogisticalInformation orderLogisticalInformation;
	    private RequestedQuantity requestedQuantity;
}
