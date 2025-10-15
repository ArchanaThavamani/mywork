/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.hibernate.annotations.ColumnDefault;

/**
 * @author mohan.e
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(schema = "commonbc",name = "tbl_product")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCProductTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "storehash")
    private String storehash;

    @Column(name = "source_product_id")
    private Integer sourceProductId;

    @Column(name = "destination_Product_id")
    private Integer destinationProductId;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "sku")
    private String sku;
    @Column(name = "description")
    private String description;
    @Column(name = "weight")
    private double weight;
    @Column(name = "width")
    private double width;
    @Column(name = "depth")
    private double depth;
    @Column(name = "height")
    private double height;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "cost_price")
    private BigDecimal costPrice;
    @Column(name = "retail_price")
    private BigDecimal retailPrice;
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    @Column(name = "tax_class_id")
    private Integer taxClassId;
    @Column(name = "product_tax_code")
    private String productTaxCode;
    @Column(name = "brand_id")
    private Integer brandId;
    @Column(name = "inventory_level")
    private Integer inventoryLevel;
    @Column(name = "inventory_warning_level")
    private Integer inventoryWarningLevel;
    @Column(name = "inventory_tracking")
    private String inventoryTracking;
    @Column(name = "fixed_cost_shipping_price")
    private BigDecimal fixedCostShippingPrice;
    @Column(name = "is_free_shipping")
    private boolean isFreeShipping;
    @Column(name = "is_visible")
    private boolean isVisible;
    @Column(name = "is_featured")
    private boolean isFeatured;
    @Column(name = "warranty")
    private String warranty;
    @Column(name = "bin_picking_number")
    private String binPickingNumber;
    @Column(name = "layout_file")
    private String layoutFile;
    @Column(name = "upc")
    private String upc;
    @Column(name = "availability")
    private String availability;
    @Column(name = "search_keywords")
    private String searchKeywords;
    @Column(name = "availability_description")
    private String availabilityDescription;
    @Column(name = "gift_wrapping_options_type")
    private String giftWrappingOptionsType;
    //  @Column(name="gift_wrapping_options_list")
    //  private ArrayList<Integer> giftWrappingOptionsList;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "condition")
    private String condition;
    @Column(name = "is_condition_shown")
    private boolean isConditionShown;
    @Column(name = "order_quantity_minimum")
    private Integer orderQuantityMinimum;
    @Column(name = "order_quantity_maximum")
    private Integer orderQuantityMaximum;
    @Column(name = "page_title")
    private String pageTitle;
    //    @Column(name = "meta_keywords")
//    private String metaKeywords;
    @Column(name = "meta_description")
    private String metaDescription;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "preorder_release_date")
    private String preorderReleaseDate;
    @Column(name = "preorder_message")
    private String preOrderMessage;
    @Column(name = "is_Preorder_only")
    private boolean isPreorderOnly;
    @Column(name = "is_price_hidden")
    private boolean isPriceHidden;
    @Column(name = "price_hidden_label")
    private String priceHiddenLabel;
    @Column(name = "custom_url")
    private String customUrl;
    @Column(name = "is_customized")
    private boolean isCustomized;
    @Column(name = "open_graph_type")
    private String openGraphType;
    @Column(name = "open_graph_title")
    private String openGraphTitle;
    @Column(name = "open_graph_description")
    private String openGraphDescription;
    @Column(name = "open_graph_use_meta_description")
    private boolean openGraphUseMetaDescription;
    @Column(name = "open_graph_use_product_name")
    private boolean openGraphUseProductName;
    @Column(name = "open_graph_use_image")
    private boolean openGraphUseImage;
    @Column(name = "brand_name")
    private String brandName;
    @Column(name = "gtin")
    private String gtin;
    @Column(name = "mpn")
    private String mpn;
    @Column(name = "calculated_price")
    private double calculatedPrice;
    @Column(name = "reviews_rating_sum")
    private Integer reviewsRatingSum;
    @Column(name = "reviews_count")
    private Integer reviewsCount;
    @Column(name = "total_sold")
    private Integer totalSold;
    @Column(name = "destination_option_set_id")
    private Integer destination_option_set_id;
    @Column(name = "option_set_name")
    private String optionSetName;
    @Column(name = "date_created")
    private Integer dateCreated;
    @Column(name = "date_modified")
    private Integer dateModified;
    @Column(name = "is_inventory_available")
    private boolean inventoryAvailable;
    @Column(name = "product_status")
    private String productStatus;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;

}
