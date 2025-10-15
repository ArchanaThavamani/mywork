/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductCustomFieldTransaction;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author mohan.e
 */
public interface PCProductCustomFieldTransactionRepository extends JpaRepository<PCProductCustomFieldTransaction, Integer> {

    public List<PCProductCustomFieldTransaction> findByProductIdAndStatus(BigInteger productId, String status);

     Optional<PCProductCustomFieldTransaction> findByProductIdAndName(BigInteger productId, String name);
    
    public List<PCProductCustomFieldTransaction> findByStatus(String status);

    public List<PCProductCustomFieldTransaction> findByNameAndValue(String name, String value);
    public PCProductCustomFieldTransaction findFirstByNameAndProductId(String name, BigInteger productId);

    Optional<PCProductCustomFieldTransaction> findByProductIdAndNameAndIsActive(BigInteger valueOf, String name, boolean b);
    
    public List<PCProductCustomFieldTransaction> findByProductIdAndIsActive(BigInteger productId , boolean isActive);
    
    Optional<PCProductCustomFieldTransaction> findByProductIdAndNameAndValue(BigInteger productId, String name, String value);     

}
