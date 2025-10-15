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
 * @author sasikumar.a
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "commonbc",name = "tbl_product_category_mapping")
public class PCProductCategoryMapping implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_mapping_id")
    private Integer categoryMappingId;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "status")
    private String status;

}
