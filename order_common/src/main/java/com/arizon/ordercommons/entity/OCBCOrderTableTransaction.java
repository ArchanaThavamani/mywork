/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "commonbc",name = "tbl_order")
public class OCBCOrderTableTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    public Integer orderId;
    @Column(name = "destination_order_id")
    public String destinationOrderId;
    @Column(name = "source_order_id")
    public Integer sourceOrderId;
    @Column(name = "email_address")
    public String emailAddress;
    @Column(name = "order_status")
    public String orderStatus;
    @Column(name = "currency")
    public String currency;
    @Column(name = "shipping_amount")
    public BigDecimal shippingAmount;
    @Column(name = "shipping_amount_inc_tax")
    public BigDecimal shippingAmountIncTax;
    @Column(name = "tax_amount")
    public BigDecimal taxAmount;
    @Column(name = "subtotal")
    public BigDecimal subtotal;
    @Column(name = "total_exc_tax")
    public BigDecimal totalExcTax;
    @Column(name = "total_inc_tax")
    public BigDecimal totalIncTax;
    @Column(name = "discount_amount")
    public BigDecimal discountAmount;
    @Column(name = "order_notes")
    public String orderNotes;
    @Column(name = "payment_method")
    public String paymentMethod;
    @Column(name = "refunded_amount")
    public BigDecimal refundedAmount;
    @Column(name = "coupon_discount_amount")
    public BigDecimal couponDiscountAmount;
    @Column(name = "gift_certificate_amount")
    public BigDecimal giftCertificateAmount;
    @Column(name = "staff_notes")
    public String staffNotes;
    @Column(name = "billing_firstname")
    public String billingFirstname;
    @Column(name = "billing_lastname")
    public String billingLastname;
    @Column(name = "billing_address_1")
    public String billingAddress1;
    @Column(name = "billing_address_2")
    public String billingAddress2;
    @Column(name = "billing_city")
    public String billingCity;
    @Column(name = "billing_state")
    public String billingState;
    @Column(name = "billing_country")
    public String billingCountry;
    @Column(name = "billing_zip")
    public String billingZip;
    @Column(name = "billing_company")
    public String billingCompany;
    @Column(name = "billing_phone")
    public String billingPhone;
    @Column(name = "billing_email")
    public String billingEmail;
    @Column(name = "billing_country_iso2")
    public String billingCountryIso2;
    @Column(name = "order_cogs")
    public BigDecimal orderCogs;
    @Column(name = "order_profit")
    public BigDecimal orderProfit;
    @Column(name = "subtotal_exc_tax")
    public BigDecimal subtotalExcTax;
    @Column(name = "is_single_shipping_address")
    public boolean isSingleShippingAddress;
    @Column(name = "destination_customer_id")
    public String destinationCustomerId;
    @Column(name = "source_customer_id")
    public Integer sourceCustomerId;
    @Column(name = "is_active")
    public boolean isActive;
    @Column(name = "is_deleted")
    public boolean isDeleted;
    @Column(name = "created_by")
    public Integer createdBy;
    @Column(name = "modified_by")
    public Integer modifiedBy;
    @Column(name = "order_created_date")
    public String orderCreatedDate;
    @Column(name = "order_modified_date")
    public String orderModifiedDate;
    @Column(name = "order_refunded_date")
    public Integer orderRefundedDate;
    @Column(name = "order_paid_date")
    public Integer orderPaidDate;
    @Column(name = "order_cancelled_date")
    public Integer orderCancelledDate;
    @Column(name = "integration_status")
    public String integrationStatus;
    @Column(name = "merchant_comment")
    public String merchantComment;
    @Column(name = "order_is_deleted")
    public boolean order_is_deleted;
    @Column(name = "store_credit")
    public BigDecimal storeCredit;
    @Column(name = "retry_count")
    public Integer retryCount;
    @Column(name = "order_reference_number")
    private Integer orderReferenceNumber;
    @Column(name = "storehash")
	private String storehash;
    @Column(name = "sourceVendor")
    private String sourceVendor;
}
