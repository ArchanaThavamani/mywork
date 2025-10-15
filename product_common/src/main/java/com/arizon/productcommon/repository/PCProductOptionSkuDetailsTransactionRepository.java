/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionSkuDetailsTransaction;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavinkumar.s
 */
public interface PCProductOptionSkuDetailsTransactionRepository extends JpaRepository<PCProductOptionSkuDetailsTransaction, Integer> {

    List<PCProductOptionSkuDetailsTransaction> findByProductOptionSkuID(int productOptionSkuID);

    public List<PCProductOptionSkuDetailsTransaction> findAllByOptionValueIDInAndProductIDAndIsActive(List<Integer> optionValueIds, int product_id, boolean isActive);

    Optional<PCProductOptionSkuDetailsTransaction> findByProductIDAndProductOptionSkuID(int product_id, int product_option_sku_id);

    Optional<PCProductOptionSkuDetailsTransaction> findByProductIDAndProductOptionSkuIDAndIsActive(int product_id, int product_option_sku_id, boolean isActive);

    Optional<PCProductOptionSkuDetailsTransaction> findByProductIDAndProductOptionSkuIDAndOptionNameAndOptionID(int product_id, int product_option_sku_id, String option_name, int product_option_id);

    Optional<PCProductOptionSkuDetailsTransaction> findByProductIDAndOptionIDAndIsActive(int product_id, int product_option_id, boolean isActive);

//    Optional<ProductOptionSkuDetailsTransaction> findByProductOptionSkuID(int product_option_sku_id);

     Optional<PCProductOptionSkuDetailsTransaction> findByOptionValueIDAndIsActive(BigInteger optionValueId, boolean isActive);
}
