/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCBrandTransaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mohan.e
 */
public interface PCBrandTransactionRepository extends JpaRepository<PCBrandTransaction, Integer> {


    public Optional<PCBrandTransaction> findByDestinationBrandIdAndStorehash(Integer brandId, String storehash);
    public Optional<PCBrandTransaction> findFirstByNameAndStorehashAndIsActiveAndIsDeleted(String brandhName, String storehash, boolean active, boolean deleted);

    public Optional<PCBrandTransaction> findByStorehashAndDestinationBrandId(String storehash, Integer destBrandId);

    public List<PCBrandTransaction> findByStatusAndStorehash(String status, String storehash);

    public Optional<PCBrandTransaction> findByName(String ProductName);


}
