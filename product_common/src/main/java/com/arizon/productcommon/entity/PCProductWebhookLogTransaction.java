package com.arizon.productcommon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_webhook_log")
public class PCProductWebhookLogTransaction {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_webhook_log_id")
	private Integer productWebhookLogId;
	
	@Column(name="destination_product_id")
	private Integer destinationProductId;
	
	@Column(name = "destination_sku_id")
	private Integer destinationSkuId;
	
	@Column(name = "scope")
	private String scope;
	
	@Column(name = "webhook_flag_status")
	private String webhookFlagStatus;
	
//	@Column(name = "inserted_date")
//	private String insertedDate;

	@Column(name = "is_active")
	public boolean isActive;

	@Column(name = "is_deleted")
	public boolean isDeleted;

	@Column(name = "status")
	public String status;

	@Column(name = "retry_count")
	public Integer retryCount;

	@Column(name = "storehash")
	private String storehash;

}
