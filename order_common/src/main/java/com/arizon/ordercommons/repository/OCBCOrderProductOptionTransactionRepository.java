/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;


import com.arizon.ordercommons.entity.OCBCOrderProductOptionTableTransaction;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kalaivani.r
 */

public interface OCBCOrderProductOptionTransactionRepository extends JpaRepository<OCBCOrderProductOptionTableTransaction, Integer> {
   Optional<OCBCOrderProductOptionTableTransaction> findBySourceOrderLineitemOptionIdAndOrderLineitemDetailsId(int orderProductOptionId, int orderProductDetailsId);

}
