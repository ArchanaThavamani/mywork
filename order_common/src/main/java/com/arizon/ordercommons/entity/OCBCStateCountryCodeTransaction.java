/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

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
 * @author kavinkumar.s
 */
@Entity
@Table(schema = "commonbc",name = "tbl_country_state_mapping_code")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCBCStateCountryCodeTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_state_mapping_code_id")
    private Integer countryStateMappingCodeID;
    @Column(name = "code")
    private String code;
    @Column(name = "state")
    private String state;
    @Column(name = "country")
    private String country;
}
