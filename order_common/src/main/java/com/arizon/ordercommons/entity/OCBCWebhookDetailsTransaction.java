/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Entity
@Table(schema = "commonbc",name = "tbl_webhook_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCBCWebhookDetailsTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webhook_details_id")
    private BigDecimal webhookDetailsID;
    @Column(name = "bc_webhook_id")
    private BigDecimal bcWebhookID;
    @Column(name = "scope")
    private String scope;
    @Column(name = "destination")
    private String destination;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_delete")
    private boolean isDelete;

}
