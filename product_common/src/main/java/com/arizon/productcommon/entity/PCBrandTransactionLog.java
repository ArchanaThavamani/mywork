/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

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
 * @author mohan.e
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_brand_ds_log")
public class PCBrandTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_ds_log_id")
    private Integer brandDsLogId;
    @Column(name = "brand_id")
    private Integer brandId;
    @Column(name = "destination_brand_id")
    private Integer destinationBrandId;
    @Column(name = "status")
    private String status;
    @Column(name = "response")
    private String response;
    @Column(name = "request")
    private String request;
    @Column(name = "type")
    private String type;

}
