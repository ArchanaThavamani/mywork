package com.arizon.racetrac_salesorder.model;

import java.util.List;
import com.arizon.racetrac_salesorder.model.*;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class OrderReleaseResponseWrapper {
	 private String timestamp;
	    private Meta meta;
	    private int totalCount;
	    private List<OrderReleaseEntity> entities;
}
