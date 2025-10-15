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

/**
 *
 * @author kavinkumar.s
 */
@Entity
@Table(schema = "commonbc",name = "tbl_customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCBCCustomerTableTransaction implements Serializable {
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerID;
    @Column(name = "source_customer_id")
    private String sourceCustomerID;
    @Column(name = "destination_customer_id")
    private Integer destinationCustomerID;
    @Column(name = "force_reset")
    private boolean forceReset;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "phone")
    private String phone;
    @Column(name = "registration_ip_address")
    private String registrationIpAddress;
    @Column(name = "customer_group_id")
    private Integer customerGroupID;
    @Column(name = "notes")
    private String notes;
    @Column(name = "tax_exempt_category")
    private String taxExemptCategory;
    @Column(name = "accepts_marketing")
    private boolean acceptsMarketing;
    @Column(name = "reset_pass_on_login")
    private boolean resetPassOnLogin;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "customer_role_id")
    private Integer customerRoleID;
    @Column(name = "dnb_reference")
    private String dnbReference;    ////  from here
    @Column(name = "is_po_require")
    private boolean isPoRequire;
    @Column(name = "preferred_warehouse")
    private String preferredWarehouse;
    @Column(name = "license_no")
    private String licenseNo;
    @Column(name = "tax_registration_no")
    private String taxRegistrationNo;
    @Column(name = "classification")
    private String classification;
    @Column(name = "tax_exempt_certificate_number")
    private String taxExemptCertificateNumber;
    @Column(name = "parent_customer_id")
    private Integer parentCustomerID;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "status")
    private String status;
    @Column(name = "store_credit")
    private BigDecimal storeCredit;
    @Column(name = "storehash")
	private String storehash;
}
