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
@Table(schema = "commonbc",name = "tbl_brand")
public class PCBrandTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Integer brandId;

    @Column(name = "storehash")
    private String storehash;

    @Column(name = "destination_brand_id")
    private Integer destinationBrandId;
    @Column(name = "name")
    private String name;
    @Column(name = "page_title")
    private String pageTitle;
    @Column(name = "search_keywords")
    private String searchKeywords;
    //@Column(name="meta_keywords")
//private String metaKeywords; 
    @Column(name = "meta_description")
    private String metaDescription;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "custom_url")
    private String customUrl;
    @Column(name = "is_customized")
    private boolean isCustomized;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    //@Column(name="created_date")
//private String createdDate; 
//@Column(name="modified_date")
//private String modifiedDate;
    @Column(name = "status")
    private String status;

}
