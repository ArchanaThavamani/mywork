package com.arizon.racetrac_salesorder.model;
import com.arizon.racetrac_salesorder.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderReleaseEntity {
	private FirstMeta meta;
	 private OrderRelease orderRelease;

	    public OrderRelease getOrderRelease() {
	        return orderRelease;
	    }

	    public void setOrderRelease(OrderRelease orderRelease) {
	        this.orderRelease = orderRelease;
	    }
}
