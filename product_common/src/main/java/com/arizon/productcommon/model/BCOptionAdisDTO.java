/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

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
public class BCOptionAdisDTO {
    public String option_name;
    public String option_Value;
    
    public String sort_order;
    
}