package com.arizon.Guidant.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_partner_function", schema = "commonbc")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerFunctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private String customerId;  // maps to BusinessPartner

    @Column(name = "customer")
    private String customer;

    @Column(name = "sales_organization")
    private String salesOrganization;

    @Column(name = "distribution_channel")
    private String distributionChannel;

    @Column(name = "division")
    private String division;

    @Column(name = "partner_counter")
    private String partnerCounter;

    @Column(name = "partner_function")
    private String partnerFunction;

    @Column(name = "bp_customer_number")
    private String bpCustomerNumber;

    @Column(name = "customer_partner_description")
    private String customerPartnerDescription;

    @Column(name = "default_partner")
    private Boolean defaultPartner;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "personnel_number")
    private String personnelNumber;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "authorization_group")
    private String authorizationGroup;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    @Column(name = "modified_date")
    private OffsetDateTime modifiedDate;
}
