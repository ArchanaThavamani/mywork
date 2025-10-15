package com.arizon.productcommon.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeData {
	public int category_id;
	public String category_uuid;
    public int parent_id;
    public int tree_id;
    public String name;
    public String description;
    public int views;
    public int sort_order;
    public String page_title;
    public ArrayList<String> meta_keywords;
    public String meta_description;
    public String layout_file;
    public String image_url;
    public boolean is_visible;
    public String search_keywords;
    public String default_product_sort;
    @JsonProperty("url") 
    public UrlDTO custom_url;
}

