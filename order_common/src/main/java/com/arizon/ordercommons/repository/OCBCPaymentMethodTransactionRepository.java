/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCBCPaymentMethodTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCPaymentMethodTransactionRepository extends JpaRepository<OCBCPaymentMethodTransaction, Integer> {

     Optional<OCBCPaymentMethodTransaction> findByPaymentMethodName(String paymentMethodName);


}
