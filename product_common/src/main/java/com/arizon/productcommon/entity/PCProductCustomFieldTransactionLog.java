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
@Table(schema = "commonbc",name="tbl_product_custom_fields_ds_log")
public class PCProductCustomFieldTransactionLog implements Serializable {
   

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) 
@Column(name="product_custom_fields_ds_log_id")
private Integer productCustomFieldsDsLogId;
@Column(name="product_custom_fields_id")
private Integer productCustomFieldsId;
@Column(name="destination_product_custom_fields_id")
private Integer destinationProductCustomFieldsId;
@Column(name="name")
private String name;
@Column(name="status")
private String status;
@Column(name="response")
private String response;
@Column(name="request")
private String request;
@Column(name="type")
private String type;
//@Column(name="created_date")
//private String createdDate;
//@Column(name="modified_date")
//private String modifiedDate;

}
