package com.arizon.productcommon.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeResponseDTO {
	public ArrayList<CategoryTreeData> data;
	public MetaDTO meta;
}
