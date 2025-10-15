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
@Table(schema = "commonbc",name = "tbl_product_options")
public class PCProductOptionTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_options_id")
    private Integer productOptionsID;
    @Column(name = "product_id")
    private BigInteger productID;
    @Column(name = "destination_option_id")
    private BigInteger destinationOptionID;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "type")
    private String type;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "destination_option_assign_id")
    private Integer destinationOptionAssignID;
    @Column(name = "status")
    private String status;
}
