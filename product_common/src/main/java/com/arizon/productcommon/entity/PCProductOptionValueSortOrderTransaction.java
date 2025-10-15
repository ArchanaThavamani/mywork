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
@Table(schema = "commonbc",name = "tbl_product_option_value_sort_order")
public class PCProductOptionValueSortOrderTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_value_sort_order_id")
    public Integer productOptionValueSortOrderId;
    @Column(name = "product_type")
    public String productType;
    @Column(name = "product_option_name")
    public String productOptionName;
    @Column(name = "value")
    public String value;
    @Column(name = "sort_order")
    public String sortOrder;
    @Column(name = "is_default")
    public boolean isDefault;
    @Column(name = "is_active")
    public boolean isActive;
    @Column(name = "is_deleted")
    public boolean isDeleted;
}
