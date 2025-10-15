/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;

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
@Entity
@Table(schema = "commonbc",name = "tbl_shipping_address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCBCShippingAddressTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Integer shippingAddressID;
    @Column(name = "shipping_firstname")
    private String shippingFirstName;
    @Column(name = "shipping_lastname")
    private String shippingLastName;
    @Column(name = "shipping_address_1")
    private String shippingAddress1;
    @Column(name = "shipping_address_2")
    private String shippingAddress2;
    @Column(name = "shipping_city")
    private String shippingCity;
    @Column(name = "shipping_state")
    private String shippingState;
    @Column(name = "shipping_country")
    private String shippingCountry;
    @Column(name = "shipping_zip")
    private String shippingZip;
    @Column(name = "shipping_company")
    private String shippingCompany;
    @Column(name = "shipping_phone")
    private String shippingPhone;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "shipping_address_code")
    private String shippingAddressCode;
    @Column(name = "source_order_lineitem_id")
    private Integer sourceOrderLineitemID;
    @Column(name = "is_code_exists")
    private boolean isCodeExists;
    @Column(name = "order_id")
    private Integer orderID;
    @Column(name = "shipping_method")
    private String shippingMethod;
}
