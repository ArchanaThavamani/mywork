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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BCOrderResponseDTO {
    public int id;
    public Integer customer_id;
    public String date_created;
    public String date_modified;
    public String date_shipped;
    public int status_id;
    public String status;
    public String subtotal_ex_tax;
    public String subtotal_inc_tax;
    public String subtotal_tax;
    public String base_shipping_cost;
    public String shipping_cost_ex_tax;
    public String shipping_cost_inc_tax;
    public String shipping_cost_tax;
    public int shipping_cost_tax_class_id;
    public String base_handling_cost;
    public String handling_cost_ex_tax;
    public String handling_cost_inc_tax;
    public String handling_cost_tax;
    public int handling_cost_tax_class_id;
    public String base_wrapping_cost;
    public String wrapping_cost_ex_tax;
    public String wrapping_cost_inc_tax;
    public String wrapping_cost_tax;
    public int wrapping_cost_tax_class_id;
    public String total_ex_tax;
    public String total_inc_tax;
    public String total_tax;
    public int items_total;
    public int items_shipped;
    public String payment_method;
    public String payment_provider_id;
    public String payment_status;
    public String refunded_amount;
    public boolean order_is_digital;
    public String store_credit_amount;
    public String gift_certificate_amount;
    public String ip_address;
    public String ip_address_v6;
    public String geoip_country;
    public String geoip_country_iso2;
    public int currency_id;
    public String currency_code;
    public String currency_exchange_rate;
    public int default_currency_id;
    public String default_currency_code;
    public String staff_notes;
    public String customer_message;
    public String discount_amount;
    public String coupon_discount;
    public int shipping_address_count;
    public boolean is_deleted;
    public String ebay_order_id;
    public String cart_id;
    public BillingAddressDTO billing_address;
    public boolean is_email_opt_in;
    public Object credit_card_type;
    public String order_source;
    public int channel_id;
    public String external_source;
    public BCOrderProductsDTO products;
    public ShippingAddressesDTO shipping_addresses;
    public CouponsDTO coupons;
    public String external_id;
    public ExternalMerchantIdDTO external_merchant_id;
    public String tax_provider_id;
    public String store_default_currency_code;
    public String store_default_to_transactional_exchange_rate;
    public String custom_status;
    public String customer_locale;
    public String external_order_id;
    
}
