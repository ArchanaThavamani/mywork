/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kavinkumar.s
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSkuDTO {

    private double cost_price;
    private double price;
    private double sale_price;
    private double retail_price;
    private BigDecimal weight;
    private double width;
    private double height;
    private double depth;
    private boolean is_free_shipping;
    private double fixed_cost_shipping_price;
    private boolean purchasing_disabled;
    private String purchasing_disabled_message;
    private String upc;
    private int inventory_level;
    private int inventory_warning_level;
    private String bin_picking_number;
    private String image_url;
    private String gtin;
    private String mpn;
    private Integer product_id;
    private String sku;
    private ArrayList<OptionValue> option_values;
}
