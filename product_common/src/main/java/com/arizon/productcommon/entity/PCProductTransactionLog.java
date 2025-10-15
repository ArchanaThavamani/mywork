/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

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
 * @author mohan.e
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_ds_log")
public class PCProductTransactionLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_ds_log_id")
    private Integer productDsLogId;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "destination_product_id")
    private Integer destinationProductId;
    @Column(name = "destination_option_set_id")
    private Integer destinationOptionSetId;
    @Column(name = "status")
    private String status;
    @Column(name = "response")
    private String response;
    @Column(name = "request")
    private String request;
    @Column(name = "type")
    private String type;
//    @Column(name = "created_date")
//    private String createdDate;
//    @Column(name = "modified_date")
//    private String modifiedDate;

//    @Column(name = "source_product_id")
//    private Integer sourceProductID;

//    @Column(name = "interface")
//    private String interfaceType;
}
