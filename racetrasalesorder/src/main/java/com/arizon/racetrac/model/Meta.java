package com.arizon.racetrac.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
	 private String entityType;
	    private String modelVersion;
	    private List<String> roles;
	    private String businessId;
	    private String createdDateTime;
	    private String ingestionId;
	    private List<String> ingestionIdHistory;
	    private String payloadCreationDateTime;
	    private String tenantId;
	    private String modifiedDateTime;
	    private String model;
	    private String processDateTime;

	    @JsonProperty("content-type")
	    private String contentType;

	    private String messageVersion;
	    private String user;
	    private String status;

}
