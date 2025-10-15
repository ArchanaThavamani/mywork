/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.ordercommons.entity.OCBCCustomerTableTransaction;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBCCustomerTransactionRepository extends JpaRepository<OCBCCustomerTableTransaction, Integer>{
    
     Optional<OCBCCustomerTableTransaction> findByStorehashAndDestinationCustomerID(String storehash,int destinationCustomerID);
    
     Optional<OCBCCustomerTableTransaction> findByStorehashAndEmailAddress(String storehash, String emailAddress);
    
    
     Optional<OCBCCustomerTableTransaction> findByStorehashAndEmailAddressAndIsActive(String storehash, String emailAddress, boolean isActive);
}
