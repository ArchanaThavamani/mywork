/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author sasikumar.a
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumDTO {

    public int id;
    public String order_id;
    public String event;
    public String method;
    public double amount;
    public String currency;
    public String gateway;
    public String gateway_transaction_id;
    public Date date_created;
    public boolean test;
    public String status;
    public boolean fraud_review;
    public int reference_transaction_id;
    public OfflineDTO offline;
    public CustomDTO custom;
    public String payment_method_id;
    public String payment_instrument_token;
    public AvsResultDTO avs_result;
    public CvvResultDTO cvv_result;
    public CreditCardDTO credit_card;
    public GiftCertificateDTO gift_certificate;
    public StoreCreditDTO store_credit;
}
