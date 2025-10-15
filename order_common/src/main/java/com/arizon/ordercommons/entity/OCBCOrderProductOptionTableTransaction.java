/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author kalaivani.r
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_order_lineitem_option_details")
public class OCBCOrderProductOptionTableTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_lineitem_option_details_id")
    private Integer orderLineitemOptionDetailsId;
    @Column(name = "order_lineitem_details_id")
    private Integer orderLineitemDetailsId;
    @Column(name = "sku")
    private String sku;
    @Column(name = "product_options_id")
    private Integer product_options_id;
    @Column(name = "product_option_value_id")
    private Integer product_option_value_id;
    @Column(name = "product_options_name")
    private String productOptionsName;
    @Column(name = "product_option_value_name")
    private String productOptionValueName;
    @Column(name = "destination_order_lineitem_option_id")
    private Integer destinationOrderLineitemOptionId;
    @Column(name = "source_order_lineitem_option_id")
    private Integer sourceOrderLineitemOptionId;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;

}
