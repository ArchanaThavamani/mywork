package com.arizon.racetrac_salesorder.model;

import java.util.List;
import com.arizon.racetrac_salesorder.model.*;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FirstMeta {
	private String self;
    private List<Link> links;

}
