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
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_payment_method")
public class OCBCPaymentMethodTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Integer paymentMethodID;
    @Column(name = "source_payment_method_id")
    private String sourcePaymentMethodID;
    @Column(name = "bc_payment_method_id")
    private Integer bcPaymentMethodID;
    @Column(name = "payment_method_name")
    private String paymentMethodName;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
//    @Column(name = "created_date")
//    private String createdDate;
//    @Column(name = "modified_date")
//    private String modifiedDate;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "source_payment_type_id")
    private Integer sourcePaymentTypeID;
    @Column(name = "customer_detail")
    private String customerDetail;
}
