/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import java.io.Serializable;
import java.util.List;
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
@Table(schema = "commonbc",name = "tbl_category")
public class PCCategoryTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "storehash")
    private String storehash;

    @Column(name = "destination_category_id")
    private Integer destinationCategoryId; //bc category id
    @Column(name = "destination_parent_category_id")
    private Integer destinationParentCategoryId; //bc parent id
    @Column(name = "name")
    private String name; //
    @Column(name = "description")
    private String description;
    @Column(name = "views")
    private Integer views;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "page_title")
    private String pageTitle;
    @Column(name = "search_keywords")
    private String searchKeywords;
    @Column(name = "meta_keywords")
    private String metaKeywords;
    @Column(name = "meta_description")
    private String metaDescription;
    @Column(name = "layout_file")
    private String layoutFile;
    @Column(name = "is_visible")
    private boolean isVisible;
    @Column(name = "default_product_sort")
    private String defaultProductSort;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "custom_url")
    private String customUrl;
    @Column(name = "is_customized")
    private boolean isCustomized;
    @Column(name = "is_active")
    private boolean isActive;//
    @Column(name = "is_deleted")
    private boolean isDeleted;//
    @Column(name = "status")
    private String status;//
    @Column(name = " parent_category_id")
    private Integer parentCategoryId; //doubt
    
    @Column(name = "source_category_id")
    private Integer sourceCategoryId;
    
//    @Column(name = "source_parent_category_id")
//    private Integer sourceParentCategoryId;
//    @Column(name = "meta_keywords")
//    private List metaKeywords;
//    @Column(name = "product_id")
//    private Integer productId;

    public Object stream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
