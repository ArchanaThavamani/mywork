/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(schema = "commonbc",name = "tbl_product_image")
public class PCProductImageTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Integer productImageId;
    @Column(name = "destination_product_image_id")
    private Integer destinationProductImageId;
    @Column(name = "product_id")
    private BigInteger productId;
    @Column(name = "product_option_sku_id")
    private BigInteger productOptionSkuId;
    @Column(name = "product_option_rule_id")
    private BigInteger productOptionRuleId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "is_main_image")
    private boolean isMainImage;
    @Column(name = "image_file")
    private String imageFile;
    @Column(name = "url_zoom")
    private String urlZoom;
    @Column(name = "url_standard")
    private String urlStandard;
    @Column(name = "url_thumbnail")
    private String urlThumbnail;
    @Column(name = "url_tiny")
    private String urlTiny;
    @Column(name = "is_thumbnail")
    private boolean isThumbnail;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
