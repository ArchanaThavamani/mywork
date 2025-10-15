/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    
    public Integer id;
    public String name;
    public String type;
    public String sku;
    public String description;
    public Double weight;
    public Double width;
    public Double depth;
    public Double height;
    public Double price;
    public Double cost_price;
    public Double retail_price;
    public Double sale_price;
    public Double map_price;
    public Integer tax_class_id;
    public String product_tax_code;
    public List<Integer> categories;
    public Integer brand_id;
    public Integer inventory_level;
    public Integer inventory_warning_level;
    public String inventory_tracking;
    public Double fixed_cost_shipping_price;
    public Boolean is_free_shipping;
    public Boolean is_visible;
    public Boolean is_featured;
    public ArrayList<Integer> related_products;
    public String warranty;
    public String bin_picking_number;
    public String layout_file;
    public String upc;
    public String search_keywords;
    public String availability_description;
    public String availability;
    public String gift_wrapping_options_type;
    public ArrayList<Integer> gift_wrapping_options_list;
    public Integer sort_order;
    public String condition;
    public Boolean is_condition_shown;
    public Integer order_quantity_minimum;
    public Integer order_quantity_maximum;
    public String page_title;
    public ArrayList<String> meta_keywords;
    public String meta_description;
    public Integer view_count;
    public Date preorder_release_date;
    public String preorder_message;
    public Boolean is_preorder_only;
    public Boolean is_price_hidden;
    public String price_hidden_label;
    public CustomUrlDTO custom_url;
    public String open_graph_type;
    public String open_graph_title;
    public String open_graph_description;
    public Boolean open_graph_use_meta_description;
    public Boolean open_graph_use_product_name;
    public Boolean open_graph_use_image;
    public String brand_name;
    public String gtin;
    public String mpn;
    public Integer reviews_rating_sum;
    public Integer reviews_count;
    public Integer total_sold;
    public ArrayList<CustomFieldDTO> custom_fields;
    public ArrayList<BulkPricingRuleDTO> bulk_pricing_rules;
    public ArrayList<ImageDTO> images;
    public ArrayList<VideoDTO> videos;
    public ArrayList<VariantDTO> variants;
    public Date date_created;
    public Date date_modified;
    public Integer base_variant_id;
    public Integer calculated_price;
    public ArrayList<OptionData> options;
//    public ArrayList<ModifierDTO> modifiers;
    public Integer option_set_id;
    public String option_set_display;
  
    public Object primary_image;
    public ArrayList<Object> reviews;
    public ArrayList<Object> modifiers;
    public ArrayList<Object> parent_relations;
    
}
