/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author kalaivani.r
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BCOrderProductResponseDTO {

    public int id;
    public int order_id;
    public int product_id;
    public int variant_id;
    public int order_pickup_method_id;
    public int order_address_id;
    public String name;
    public String name_customer;
    public String name_merchant;
    public String sku;
    public String upc;
    public String type;
    public String base_price;
    public String price_ex_tax;
    public String price_inc_tax;
    public String price_tax;
    public String base_total;
    public String total_ex_tax;
    public String total_inc_tax;
    public String total_tax;
    public String weight;
    public String width;
    public String height;
    public String depth;
    public int quantity;
    public String base_cost_price;
    public String cost_price_inc_tax;
    public String cost_price_ex_tax;
    public String cost_price_tax;
    public boolean is_refunded;
    public int quantity_refunded;
    public String refund_amount;
    public int return_id;
    public String wrapping_name;
    public String base_wrapping_cost;
    public String wrapping_cost_ex_tax;
    public String wrapping_cost_inc_tax;
    public String wrapping_cost_tax;
    public String wrapping_message;
    public int quantity_shipped;
    public String fixed_shipping_cost;
    public String ebay_item_id;
    public String ebay_transaction_id;
    public int option_set_id;
    public int parent_order_product_id;
    public boolean is_bundled_product;
    public String bin_picking_number;
    public String external_id;
    public String fulfillment_source;
    public String brand;
    public ArrayList<BCOrderAppliedDiscountDTO> applied_discounts;
    public ArrayList<BCOrderProductOptionDTO> product_options;
    public ArrayList<Object> configurable_fields;
    public String gift_certificate_id;
    public String event_name;
    public String event_date;

}
