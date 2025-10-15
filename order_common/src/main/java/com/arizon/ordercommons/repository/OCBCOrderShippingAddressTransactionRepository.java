/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCBCShippingAddressTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCOrderShippingAddressTransactionRepository extends JpaRepository<OCBCShippingAddressTransaction, Integer> {

     Optional<OCBCShippingAddressTransaction> findByorderID(int orderID);

     Optional<OCBCShippingAddressTransaction> findByShippingAddressCode(String shippingCode);
}
