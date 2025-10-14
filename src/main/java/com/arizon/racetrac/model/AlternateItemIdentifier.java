package com.arizon.racetrac.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AlternateItemIdentifier {
    @JsonProperty("descriptor")
    private String descriptor;

    @JsonProperty("id")
    private String id;
}