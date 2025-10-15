package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductWebhookDTO {
	private Long created_at;
	private String store_id;
	private String producer;
	private String scope;
	private String hash;
	private ProductWebHookDataDTO data;
}
