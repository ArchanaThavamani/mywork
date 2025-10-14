package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenResponse {
	 @JsonProperty("access_token")
	 private String accessToken;

	  @JsonProperty("token_type")
	    private String tokenType;
	
	  @JsonProperty("grant_type")
	    private String grant_type;
	  @JsonProperty("refresh_token")
	    private String refresh_token;
	  
	  public TokenResponse(String accessToken, String tokenType, String grant_type, String refresh_token) {
			super();
			this.accessToken = accessToken;
			this.tokenType = tokenType;
			this.grant_type = grant_type;
			this.refresh_token = refresh_token;
		}

        @Override
	    public String toString() {
		return "TokenResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + ", refresh_token="
				+ refresh_token + "]";
	  }
}

