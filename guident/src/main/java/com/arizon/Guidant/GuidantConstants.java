package com.arizon.Guidant;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class GuidantConstants {

	GuidantConstants() {}


    public static final String SUCCESS = "Success";
    public static final String Pending = "Pending";
    public static final String FAILED  = "Failed";

    public static final String IntegerOne = "1";
    public static final String amountZero = "0.00";
    
    

    public static final String INTEGERONE= "1";
    public static final String AMOUNTZERO = "0.00";
    public static final String ORDERINTERFACE = "SAP Order Interface";
    public static final String SAPORDERFAILURE ="SAP order Failure";
    public static final String SAPORDERID = "SalesOrder";
    public static final String SAPORDERSUCCESS = "SAP Order Success";
    public static final String SHIPTO = "SH";
    public static final String[] SKIP_STATUSES = {"05", "33", "34", "35"};
    public static final String PHYSICALTYPE = "physical";
    public static final String PRODUCTPRICEGROUP = "SalesOrgPriceGroup";
    public static final String PRODUCTSALESORGANISATION = "ProductSalesOrg";
    public static final String PRODUCTPLANT = "Plant";
   // public static final BigDecimal DEFAULT_MULTIPLIER = BigDecimal.valueOf(1.2);

}
