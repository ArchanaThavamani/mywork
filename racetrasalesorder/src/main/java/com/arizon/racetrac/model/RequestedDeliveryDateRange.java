package com.arizon.racetrac.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestedDeliveryDateRange {
	  private String beginDate;
	    private String endDate;
	    private String beginTime;
	    private String endTime;

}
