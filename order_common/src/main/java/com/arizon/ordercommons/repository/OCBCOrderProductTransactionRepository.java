/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCBCOrderProductTableTransaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OCBCOrderProductTransactionRepository extends JpaRepository<OCBCOrderProductTableTransaction, Integer> {

    public Optional<OCBCOrderProductTableTransaction> findBysourceOrderLineitemId(int lineItemId);

    public List<OCBCOrderProductTableTransaction> findByorderId(int orderID);

}
