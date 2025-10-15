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
 * @author kalaivani.r
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderWebhookStatusDTO {
    public String status;
    public String paymentProviderId;
    
}
