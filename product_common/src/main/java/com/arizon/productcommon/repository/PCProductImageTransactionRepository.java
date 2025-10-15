/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductImageTransaction;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author mohan.e
 */
public interface PCProductImageTransactionRepository extends JpaRepository<PCProductImageTransaction, Integer> {

    Optional<PCProductImageTransaction> findByProductIdAndProductOptionSkuIdAndIsActiveAndIsDeleted(BigInteger product_id, BigInteger product_option_sku_id, boolean b, boolean b0);

    List<PCProductImageTransaction> findByProductIdAndProductOptionSkuIdIsNullAndProductOptionRuleIdIsNullAndIsActiveAndIsDeleted(BigInteger product_id, boolean b, boolean b0);

    List<PCProductImageTransaction> findByProductIdAndIsActiveAndIsDeleted(BigInteger product_id,boolean isActive , boolean isDeleted);
    
    Optional<PCProductImageTransaction> findByProductIdAndDestinationProductImageIdAndIsActiveAndIsDeleted(BigInteger productId, Integer destinationProductImageId,boolean isActive , boolean isDeleted);

}
