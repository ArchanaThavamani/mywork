package com.arizon.Guidant.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuidantAwsSecrets {
	
    public String mailFrom;
    public String mailTo;
    public String mailToInt;
    public String mailSubjectPrefix;
    public Integer period;
    public Integer max_period;
    public Integer max_attempts;
}
