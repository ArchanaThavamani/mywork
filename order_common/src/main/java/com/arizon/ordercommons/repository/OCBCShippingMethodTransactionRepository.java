/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.ordercommons.entity.OCBCShippingMethodsTransaction;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCShippingMethodTransactionRepository extends JpaRepository<OCBCShippingMethodsTransaction, Integer> {

    Optional<OCBCShippingMethodsTransaction> findByshippingMethodName(String shippingMethodName);

    public OCBCShippingMethodsTransaction findByShippingMethodID(int shippingMethodId);
}
