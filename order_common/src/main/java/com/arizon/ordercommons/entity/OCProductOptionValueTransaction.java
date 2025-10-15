/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;
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
 * @author kalaivani.r
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_option_value")
public class OCProductOptionValueTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_value_id")
    private Integer productOptionValueID;
    @Column(name = "product_id")
    private BigInteger productID;
    @Column(name = "destination_option_value_id")
    private Integer destinationOptionValueID;
    @Column(name = "product_options_id")
    private BigInteger productOptionsID;
    @Column(name = "is_default")
    private boolean isDefault;
    @Column(name = "label")
    private String label;
    @Column(name = "value_data")
    private String valueData;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "modified_date")
    private Timestamp modifiedDate;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "status")
    private String status;

}
