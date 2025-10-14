package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateRange {

    @JsonProperty("beginDate")
    private String beginDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("beginTime")
    private String beginTime;

    @JsonProperty("endTime")
    private String endTime;
}

