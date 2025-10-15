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
 * @author kalaivani.r
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_option_sku_details_ds_log")
public class PCProductOptionSkuDetailsDSLogTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_sku_details_ds_log_id")
    public Integer productOptionSkuDetailsLogId;
    @Column(name = "product_option_sku_details_id")
    public Integer productOptionSkuDetailsId;
    @Column(name = "destination_product_option_sku_id")
    public Integer destinationProductOptionSkuId;
    @Column(name = "status")
    public String status;
    @Column(name = "response")
    public String resposne;
    @Column(name = "type")
    public String type;
    @Column(name = "request")
    public String request;

}
