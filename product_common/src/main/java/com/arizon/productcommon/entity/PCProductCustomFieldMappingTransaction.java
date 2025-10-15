/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

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
@Table(schema = "commonbc",name = "tbl_product_custom_fields_details")
public class PCProductCustomFieldMappingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_custom_fields_details_id")
    private Integer productCustomFieldsDetailsId;
    @Column(name = "custom_field_name")
    private String customFieldName;
    @Column(name = "product_type")
    private String productType;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
//    @Column(name = "created_date")
//    private String createdDate;
//    @Column(name = "modified_date")
//    private String modifiedDate;
  

}
