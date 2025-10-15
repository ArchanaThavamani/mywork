/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import java.io.Serializable;
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
@Table(schema = "commonbc",name = "tbl_product_options_ds_log")
public class PCProductOptionLogTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_options_ds_log_id")
    private Integer productOptionsDsLogID;
    @Column(name = "product_options_id")
    private Integer productOptionsID;
    @Column(name = "status")
    private String status;
    @Column(name = "response")
    private String response;
//    @Column(name = "created_date")
//    private Timestamp createdDate;
//    @Column(name = "modified_date")
//    private Timestamp modifiedDate;
    @Column(name = "type")
    private String type;
    @Column(name = "destination_option_id")
    private Integer destinationOptionID;
    @Column(name = "is_email_sent")
    private boolean isEmailSent;
    @Column(name = "request")
    private String request;

}
