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
@Table(schema = "commonbc",name ="tbl_category_webhook_log")
public class PCCategoryWebhookLogTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_webhook_log_id")
	public Integer categoryWebhookLogId;
	@Column(name = "destination_category_id")
	public Integer destinationCategoryId;
	@Column(name = "scope")
	public String scope;
	@Column(name = "status")
	public String status;
	@Column(name = "inserted_date")
	public Integer insertedDate;
	@Column(name = "is_active")
	public boolean isActive;
	@Column(name = "is_deleted")
	public boolean isDeleted;
	@Column(name="retry_count")
	public Integer retryCount;
	@Column(name="storehash")
	public String storehash;
}
