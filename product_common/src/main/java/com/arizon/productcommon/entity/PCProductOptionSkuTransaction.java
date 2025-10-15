/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

/**
 *
 * @author kavinkumar.s
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_option_sku")
public class PCProductOptionSkuTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_sku_id")
    private Integer productOptionSkuID;

    @Column(name = "product_id")
    private Integer productID;

    @Column(name = "destination_sku_id")
    private Integer destinationSkuID;

    @Column(name = "sku")
    private String sku;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    @Column(name = "weight")
    private double weight;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "depth")
    private double depth;

    @Column(name = "is_free_shipping")
    private boolean isFreeShipping;

    @Column(name = "fixed_cost_shipping_price")
    private BigDecimal fixedCostShippingPrice;

    @Column(name = "purchasing_disabled")
    private boolean purchasingDisabled;

    @Column(name = "purchasing_disabled_message")
    private String purchasingDisabledMessage;

    @Column(name = "upc")
    private String upc;

    @Column(name = "inventory_level")
    private BigDecimal inventoryLevel;

    @Column(name = "inventory_warning_level")
    private Integer inventoryWarningLevel;

    @Column(name = "bin_picking_number")
    private String binPickingNumber;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "status")
    private String status;

    @Column(name = "product_options_id")
    private Integer productOptionsID;

    @Column(name = "image_url")
    private String imageUrl;

}
