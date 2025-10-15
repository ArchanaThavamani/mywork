/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;
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

/**
 *
 * @author kavinkumar.s
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_shipping_method")
public class OCBCShippingMethodsTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_method_id")
    private Integer shippingMethodID;
    @Column(name = "source_shipping_method_id")
    private String sourceShippingMethodID;
    @Column(name = "bc_shipping_method_id")
    private Integer bcShippingMethodID;
    @Column(name = "shipping_method_name")
    private String shippingMethodName;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
//    @Column(name = "created_date")
//    private String created_date;
//    @Column(name = "modified_date")
//    private String modified_date;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "customer_detail")
    private String customerDetail;
}
