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
 * @author kalaivani.r
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc", name = "tbl_order_webhook_log")
public class OCBCOrderWebhookTableTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_webhook_log_id")
    public Integer orderWebhookLogId;

    @Column(name = "destination_order_id")
    public Integer destinationOrderId;

    @Column(name = "scope")
    public String scope;

    @Column(name = "destination_order_created_date")
    public String destinationOrderCreatedDate;

    @Column(name = "previous_order_status")
    public String previousOrderStatus;

    @Column(name = "current_order_status")
    public String currentOrderStatus;

    @Column(name = "webhook_flag_status")
    public String webhookFlagStatus;

    @Column(name = "inserted_date")
    public String insertedDate;

    @Column(name = "is_active")
    public boolean isActive;

    @Column(name = "is_deleted")
    public boolean isDeleted;

    @Column(name = "status")
    public String status;

    @Column(name = "retry_count")
    public Integer retryCount;

    @Column(name = "storehash")
	private String storehash;
}
