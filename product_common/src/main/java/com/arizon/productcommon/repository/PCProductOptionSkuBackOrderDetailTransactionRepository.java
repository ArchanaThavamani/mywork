/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionSkuBackOrderDetailTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kalaivani.r
 */

public interface PCProductOptionSkuBackOrderDetailTransactionRepository extends JpaRepository<PCProductOptionSkuBackOrderDetailTransaction, Integer>{

     Optional<PCProductOptionSkuBackOrderDetailTransaction> findByProductOptionSkuId(int product_option_sku_id);
    
}
