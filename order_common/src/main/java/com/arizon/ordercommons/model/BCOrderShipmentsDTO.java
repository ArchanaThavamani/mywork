/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class BCOrderShipmentsDTO {

    public int id;
    public int order_id;
    public int customer_id;
    public int order_address_id;
    public String date_created;
    public String tracking_number;
    public String merchant_shipping_cost;
    public String shipping_method;
    public String comments;
    public String shipping_provider;
    public String tracking_carrier;
    public String tracking_link;

}
