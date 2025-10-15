/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionTransaction;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kavinkumar.s
 */
public interface PCProductOptionTransactionRepository extends JpaRepository<PCProductOptionTransaction, Integer> {

    public List<PCProductOptionTransaction> findByproductID(int productID);

    public List<PCProductOptionTransaction> findBystatus(String status);

     Optional<PCProductOptionTransaction> findByDisplayNameAndProductIDAndIsActive(String option_name, int product_id, boolean isActive);

     Optional<PCProductOptionTransaction> findByProductIDAndDestinationOptionAssignIDAndIsActive(BigInteger productid, int productOptionAssignId, boolean isActive);

    
}
