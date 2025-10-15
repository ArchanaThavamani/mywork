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
@Table(schema = "commonbc",name = "tbl_product_option_sku_back_order_detail")
public class PCProductOptionSkuBackOrderDetailTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_sku_back_order_detail_id")
    public Integer productOptionSkuBackOrderDetailId;
    @Column(name = "promising_date")
    public String promisingDate;
    @Column(name = "bc_to_gp_status")
    public String bcToGPStatus;
    @Column(name = "is_back_order")
    public Integer isBackOrder;
    @Column(name = "item_site_id")
    public String itemSiteId;
    @Column(name = "integration_type")
    public String integrationType;
    @Column(name = "is_active")
    public boolean isActive;
    @Column(name = "is_deleted")
    public boolean isDeleted;
    @Column(name = "product_option_sku_id")
    public Integer productOptionSkuId;

}
