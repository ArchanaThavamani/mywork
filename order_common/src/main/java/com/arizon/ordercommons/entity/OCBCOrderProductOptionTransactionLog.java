/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author kalaivani.r
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_order_lineitem_option_details_ds_log")
public class OCBCOrderProductOptionTransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_lineitem_option_details_ds_log_id")
    public Integer orderLineitemOptionDetailsDsLogId;
    @Column(name = "order_lineitem_option_details_id")
    public Integer orderLineitemOptionDetailsId;
    @Column(name = "destination_order_lineitem_option_id")
    public Integer destinationOrderLineitemOptionId;
    @Column(name = "source_order_lineitem_option_id")
    public Integer sourceOrderLineItemOptionID;
    @Column(name = "status")
    public String status;
    @Column(name = "response")
    public String response;
    @Column(name = "type")
    public String type;
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "modified_date", nullable = false, updatable = true)
    private Timestamp updatedAt;
    
    
    

}
