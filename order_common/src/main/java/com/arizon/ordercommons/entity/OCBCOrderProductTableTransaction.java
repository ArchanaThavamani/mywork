/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author kalaivani.r
 */
@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_order_lineitem_details")
public class OCBCOrderProductTableTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_lineitem_details_id")
    public Integer orderLineitemDetailsId;
    @Column(name= "source_order_lineitem_id")
    public Integer sourceOrderLineitemId;
    @Column(name = "order_id")
    public Integer orderId;
    @Column(name = "source_product_id")
    public Integer sourceProductId;
    @Column(name = "product_name")
    public String productName;
    @Column(name = "quantity")
    public Integer quantity;
    @Column(name = "sku")
    public String sku;
    @Column(name = "lineitem_price")
    public BigDecimal lineitemPrice;
    @Column(name = "lineitem_cost_price")
    public BigDecimal lineitemCostPrice;
    @Column(name = "lineitem_sale_price")
    public BigDecimal lineitemSalePrice;
    @Column(name = "lineitem_price_inc_tax")
    public BigDecimal lineitemPriceIncTax;
    @Column(name = "lineitem_tax")
    public BigDecimal lineitemTax;
    @Column(name = "lineitem_total_cost_price")
    public BigDecimal lineitemTotalCostPrice;
    @Column(name = "destination_order_lineitem_id")
    public Integer destinationOrderLineitemId;
    @Column(name = "shipping_amount")
    public BigDecimal shippingAmount;
    @Column(name = "shipping_amount_inc_tax")
    public BigDecimal shippingAmountIncTax;
    @Column(name = "shipping_method")
    public String shippingMethod;
    @Column(name = "is_gift_certificate")
    public boolean isGiftCertificate;
    @Column(name = "shipping_address_id")
    public Integer shippingAddressId;
    @Column(name = "is_active")
    public boolean isActive;
    @Column(name = "is_deleted")
    public boolean isDeleted;
    @Column(name = "created_by")
    public Integer createdBy;
    @Column(name = "modified_by")
    public Integer modifiedBy;
    @Column(name = "shipment")
    public String shipment;
    @Column(name = "tracking")
    public String tracking;
    @Column(name = "brand_name")
    public String brandName;
    @Column(name = "destination_product_id")
    public Integer destinationProductId;
    @Column(name = "bc_order_address_id")
    public Integer bcOrderAddressId;
    @Column(name = "shipping_method_id")
    public Integer shippingMethodId;
    @Column(name = "base_price")
    public BigDecimal basePrice;
    @Column(name = "base_total")
    public BigDecimal baseTotal;
    @Column(name = "shipping_firstname")
    public String shippingFirstname;
    @Column(name = "shipping_lastname")
    public String shippingLastname;
    @Column(name = "shipping_address_1")
    public String shippingAddress1;
    @Column(name = "shipping_address_2")
    public String shippingAddress2;
    @Column(name = "shipping_city")
    public String shippingCity;
    @Column(name = "shipping_state")
    public String shippingState;
    @Column(name = "shipping_country")
    public String shippingCountry;
    @Column(name = "shipping_zip")
    public String shippingZip;
    @Column(name = "shipping_company")
    public String shippingCompany;
    @Column(name = "shipping_phone")
    public String shippingPhone;
    @Column(name="coupon_discount_amt")
    public BigDecimal couponDiscountAmount;

}
