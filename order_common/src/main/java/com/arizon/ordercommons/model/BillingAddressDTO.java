/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author kalaivani.r
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingAddressDTO {
    public String first_name;
    public String last_name;
    public String company;
    public String street_1;
    public String street_2;
    public String city;
    public String state;
    public String zip;
    public String country;
    public String country_iso2;
    public String phone;
    public String email;
    public ArrayList<BCFormFieldsDTO> form_fields;
    
}
