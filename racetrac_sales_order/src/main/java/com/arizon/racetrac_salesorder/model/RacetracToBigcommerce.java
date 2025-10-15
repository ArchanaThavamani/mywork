package com.arizon.racetrac_salesorder.model;

import lombok.Data;

@Data
public class RacetracToBigcommerce {
	private String Warehouse_Id;
	private String Store_Id;
	private String Order_Id;
//	transactionalTradeItem->primaryId
	private String SKU;

}
