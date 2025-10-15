/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

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
public class BCOrderShippingResponseDTO {
    public int id;
    public int order_id;
    public String first_name;
    public String last_name;
    public String company;
    public String street_1;
    public String street_2;
    public String city;
    public String zip;
    public String country;
    public String country_iso2;
    public String state;
    public String email;
    public String phone;
    public int items_total;
    public int items_shipped;
    public String shipping_method;
    public String base_cost;
    public String cost_ex_tax;
    public String cost_inc_tax;
    public String cost_tax;
    public int cost_tax_class_id;
    public String base_handling_cost;
    public String handling_cost_ex_tax;
    public String handling_cost_inc_tax;
    public String handling_cost_tax;
    public int handling_cost_tax_class_id;
    public int shipping_zone_id;
    public String shipping_zone_name;
    public BCShippingQuotesDTO shipping_quotes;
    public ArrayList<BCFormFieldsDTO> form_fields;

}
