/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author mohan.e
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDataDTO {
    
    public int id;
    public int parent_id;
    public String name;
    public String description;
    public int views;
    public int sort_order;
    public String page_title;
    public List<String> meta_keywords;
    public String meta_description;
    public String layout_file;
    public String image_url;
    public boolean is_visible;
    public String search_keywords;
    public String default_product_sort;
    public CustomUrlDTO custom_url;
}
