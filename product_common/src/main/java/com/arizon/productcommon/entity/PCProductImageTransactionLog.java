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
@Table(schema = "commonbc",name="tbl_product_image_ds_log")
public class PCProductImageTransactionLog implements Serializable {
 
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="product_image_ds_log_id")
private Integer productImageDsLogId; 
@Column(name="product_image_id")
private Integer productImageId; 
@Column(name="destination_product_image_id")
private Integer destinationProductImageId; 
@Column(name="status")
private String status; 
@Column(name="response")
private String response; 
@Column(name="type")
private String type; 

}
