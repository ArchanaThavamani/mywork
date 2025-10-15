/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.ordercommons.entity.OCBCProductTableTransaction;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCProductTransactionRepository extends JpaRepository<OCBCProductTableTransaction, Integer> {
    
    public Optional<OCBCProductTableTransaction> findByStorehashAndSku(String storehash,String sku);
    public Optional<OCBCProductTableTransaction> findFirstByStorehashAndSku(String storehash,String sku);

}
