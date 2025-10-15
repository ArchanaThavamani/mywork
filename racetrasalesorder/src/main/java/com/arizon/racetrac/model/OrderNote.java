package com.arizon.racetrac.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class OrderNote {
	private Text text;
    private String lineNumber;
    private String type;
}
