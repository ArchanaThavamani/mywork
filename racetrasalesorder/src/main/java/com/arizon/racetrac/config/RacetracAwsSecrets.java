package com.arizon.racetrac.config;
 
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RacetracAwsSecrets {
	
    public String mailFrom;
    public String mailTo;
    public String mailToInt;
    public String mailSubjectPrefix;
    public Integer period;
    public Integer max_period;
    public Integer max_attempts;
    public String accessToken;

    @SerializedName("clientId")
    public String workdayClientId;

    @SerializedName("client_secret")
    public String workdayClientSecret;

    @SerializedName("refresh_token")
    public String workdayRefreshToken;

    @SerializedName("storehash")
    public String storeHash;

    @SerializedName("grant_type")
    public String workdayGrantType;
   
}