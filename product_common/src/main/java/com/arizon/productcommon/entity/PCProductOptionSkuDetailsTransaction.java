/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(schema = "commonbc",name = "tbl_product_option_sku_details")
public class PCProductOptionSkuDetailsTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_sku_details_id")
    private Integer productOptionSkuDetailsID;
    @Column(name = "product_id")
    private BigInteger productID;
    @Column(name = "product_option_sku_id")
    private BigInteger productOptionSkuID;
    @Column(name = "option_name")
    private String optionName;
    @Column(name = "option_id")
    private BigInteger optionID;
    @Column(name = "option_value")
    private String optionValue;
    @Column(name = "option_value_id")
    private BigInteger optionValueID;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "destination_product_option_sku_id")
    private Integer destinationProductOptionSkuID;
    @Column(name = "status")
    private String status;
}
