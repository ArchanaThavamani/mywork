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
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantDTO {
    
    public int id;
    public int sku_id;
    public double cost_price;
    public double price;
    public double sale_price;
    public double retail_price;
    public double weight;
    public double width;
    public double height;
    public double depth;
    public boolean is_free_shipping;
    public double fixed_cost_shipping_price;
    public boolean purchasing_disabled;
    public String purchasing_disabled_message;
    public String upc;
    public long inventory_level;
    public long inventory_warning_level;
    public String bin_picking_number;
    public String mpn;
    public String gtin;
    public int product_id;
    public String sku;
    public ArrayList<ProductOptionValueDTO> option_values;
    public double calculated_price;
    public double calculated_weight;
    
    public double map_price;
    public String image_url;
    
}
