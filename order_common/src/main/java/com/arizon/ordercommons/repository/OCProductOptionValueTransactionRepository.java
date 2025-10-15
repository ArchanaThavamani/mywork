/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCProductOptionValueTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kalaivani.r
 */
public interface OCProductOptionValueTransactionRepository extends JpaRepository<OCProductOptionValueTransaction, Integer> {

    public Optional<OCProductOptionValueTransaction> findByDestinationOptionValueIDAndIsActiveAndIsDeleted(int optionValueId, boolean isActive, boolean isDeleted);
    
}
