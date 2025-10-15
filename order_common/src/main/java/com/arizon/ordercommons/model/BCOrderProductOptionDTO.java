/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class BCOrderProductOptionDTO {
    public int id;
    public int option_id;
    public int order_product_id;
    public int product_option_id;
    public String display_name;
    public String display_name_customer;
    public String display_name_merchant;
    public String display_value;
    public String display_value_customer;
    public String display_value_merchant;
    public String value;
    public String type;
    public String name;
    public String display_style;
    
}
