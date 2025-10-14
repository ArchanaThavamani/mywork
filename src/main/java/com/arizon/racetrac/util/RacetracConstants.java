package com.arizon.racetrac.util;

import lombok.Data;

@Data
public class RacetracConstants {
    public static final String SUCCESS = "Success";
    public static final String FAILED  = "Failed";
    public static final String orderSubType="C";
	public static final String documentStatusCode="ORIGINAL";
	public static final String documentActionCode="ADD";
	public static final String tmCustomerCode="RTD";
	public static final String orderLinePriority="2";
	public static final boolean isSaturdayDeliveryAllowed=true;
	public static final String shipmentSplitMethod="NO_SPLIT";
	public static final String value="00000000000000";
	public static final String typeCode="BUYER_ASSIGNED";
	public static final boolean isCaseSplittingAllowed=true;
	public static final String beginTime = "00:01:00.000";
	public static final String endTime = "23:59:00.000";
}
