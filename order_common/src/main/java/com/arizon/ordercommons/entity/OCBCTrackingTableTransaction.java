/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_tracking")
public class OCBCTrackingTableTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "message_id")
    public String messageId;
    @Column(name = "storehash")
    public String storehash;
    @Column(name = "type")
    public String type;
    @Column(name = "provider")
    public String provider;
    @Column(name = "order_id")
    public String orderId;
    @Column(name = "fulfillment_id")
    public String fulfillmentId;
    @Column(name = "threeplorder_id")
    public String threeplorderId;
    @Column(name = "shipment_id")
    public String shipmentId;
    @Column(name = "invoice_id")
    public String invoiceId;
    @Column(name = "tracking_id")
    public String trackingId;
    @Column(name = "container_id")
    public String containerId;
    @Column(name = "invoice_amount")
    public Double invoiceAmount;
    @Column(name = "invoice_date")
    public Timestamp invoiceDate;
    @Column(name = "line_id")
    public String lineId;
    @Column(name = "line_sku")
    public String lineSku;
    @Column(name = "line_threeplsku")
    public String lineThreeplsku;
    @Column(name = "line_qty")
    public Double lineQty;
    @Column(name = "line_uom")
    public String lineUom;
    @Column(name = "line_price")
    public Double linePrice;
    @Column(name = "line_desc")
    public String lineDesc;
    @Column(name = "cntl_num")
    public String cntlNum;
    @Column(name = "source_file_name")
    public String sourceFileName;
    @Column(name = "dest_file_name")
    public String destFileName;
    @Column(name = "status")
    public String status;
    @Column(name = "description")
    public String description;
    @Column(name = "ack_status")
    public String ackStatus;
    @Column(name = "ack_cntl_num")
    public String aclCntlNum;
    @Column(name = "ack_file_name")
    public String ackFileName;
    @Column(name = "ack_date")
    public Timestamp ackDate;

}
