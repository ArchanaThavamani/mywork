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
 * @author kavinkumar.s
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReportFieldsDTO {

    private String sourceProductID;
    private String destinationProductID;
    private String productSku;
    private String productName;
    private String price;
    private String inventoryLevel;
    private String productStatus;
    private String integrationType;
    private String interfaceType;
}
