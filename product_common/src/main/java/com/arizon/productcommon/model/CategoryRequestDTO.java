/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryRequestDTO {
    
    public Integer parent_id;
    public String name;
    public String description;
    public Integer views;
    public Integer sort_order;
    public String page_title;
    public String search_keywords;
    public ArrayList<String> meta_keywords;
    public String meta_description;
    public String layout_file;
    public boolean is_visible;
    public String default_product_sort;
    public String image_url;
    public CustomUrlDTO custom_url;
}
