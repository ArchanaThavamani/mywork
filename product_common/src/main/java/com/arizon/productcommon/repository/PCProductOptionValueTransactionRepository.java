/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionValueTransaction;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavinkumar.s
 */
public interface PCProductOptionValueTransactionRepository extends JpaRepository<PCProductOptionValueTransaction, Integer> {

    public List<PCProductOptionValueTransaction> findByproductID(int productID);

    public List<PCProductOptionValueTransaction> findByproductOptionsID(Integer productOptionID);

    Optional<PCProductOptionValueTransaction> findByProductOptionsIDAndLabel(int product_option_id, String option_Value);

    Optional<PCProductOptionValueTransaction> findByLabelAndProductIDAndProductOptionsIDAndIsActive(String option_Value, int product_id, int product_option_id, boolean isActive);

    public List<PCProductOptionValueTransaction> findByProductIDAndProductOptionsIDAndIsActive(int product_id, int product_option_id, boolean b);

    public Optional<PCProductOptionValueTransaction> findByProductIDAndDestinationOptionValueIDAndProductOptionsIDAndIsActive(Integer productId, BigInteger valueOf, BigInteger valueOf0, boolean b);

}
