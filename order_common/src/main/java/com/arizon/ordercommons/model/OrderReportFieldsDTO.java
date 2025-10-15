/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kavinkumar.s
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportFieldsDTO {

    private String sourceOrderID;
    private String destinationOrderID;
    private String orderStatus;
    private String integrationType;
    private String notes;
    private String interfaceType;
}
