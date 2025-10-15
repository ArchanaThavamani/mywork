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
 * @author mohan.e
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_category_ds_log")
public class PCCategoryTransactionLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_ds_log_id")
    private Integer categoryDsLogId;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "parent_category_id")
    private Integer parentCategoryId;
    @Column(name = "destination_category_id")
    private Integer destinationCategoryId;
    @Column(name = "destination_parent_category_id")
    private Integer destinationParentCategoryId;
    @Column(name = "status")
    private String status;
    @Column(name = "request")
    private String request;
    @Column(name = "response")
    private String response;
    //@Column(name="created_date")
//private String createdDate;
//@Column(name="modified_date")
//private String modifiedDate;
    @Column(name = "type")
    private String type;

}
