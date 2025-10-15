/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.ordercommons.entity.OCBCWebhookDetailsTransaction;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCWebhookDetailsTransactionRepository extends JpaRepository<OCBCWebhookDetailsTransaction, BigDecimal> {

    public List<OCBCWebhookDetailsTransaction> findByisActive(boolean isActive);
}
