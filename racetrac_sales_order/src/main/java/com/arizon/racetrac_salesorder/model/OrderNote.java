package com.arizon.racetrac_salesorder.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class OrderNote {
	private Text text;
    private String lineNumber;
    private String type;
}
