/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductTransaction;

import java.util.List;
import java.util.Optional;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author mohan.e
 */
public interface PCProductTransactionRepository extends JpaRepository<PCProductTransaction, Integer> {

    public Optional<PCProductTransaction> findByStorehashAndDestinationProductIdAndIsActiveAndIsDeleted(String storehash, int productId, boolean isActive, boolean isDeleted);
    public Optional<PCProductTransaction> findByStorehashAndDestinationProductId(String storehash, int productId);

    public List<PCProductTransaction> findByProductStatusAndStorehash(String Status, String storehash);

    @Query(value = "select * from commonbc.tbl_product where created_date >=  (current_date - :days) and storehash = :storehash", nativeQuery = true)
    public List<PCProductTransaction> findByProductByStorehashAndDate(@Param("days") Integer days, @Param("storehash") String storehash);

    public Optional<PCProductTransaction> findByStorehashAndDestinationProductIdAndIsActive(String storehash, int bc_product_id, boolean isActive);

    public PCProductTransaction findFirstByStorehashAndProductIdAndIsActiveAndIsDeleted(String storehash, Integer productId, boolean isActive, boolean isDeleted);


    Optional<PCProductTransaction> findByProductId(Integer product_id);
    Optional<PCProductTransaction> findFirstByStorehashAndSku(String storehash,String sku);
    List<PCProductTransaction> findByStorehashAndSkuAndDestinationProductIdIsNull(String storehash,String sku);

    Optional<PCProductTransaction> findByProductIdAndIsActive(int product_id, boolean isActive);

    public Optional<PCProductTransaction> findBySourceProductId(int productId);

    public Optional<PCProductTransaction> findBySku(String sku);

    public Optional<PCProductTransaction> findBySourceProductIdAndIsActiveAndIsDeleted(int sourceProductId, boolean isActive, boolean isDeleted);

    public Optional<PCProductTransaction> findBySourceProductId(Integer sourceProductId);

    public Optional<PCProductTransaction> findByproductIdAndSku(Integer productId, String productSku);

    public Optional<PCProductTransaction> findByname(String productName);

    public Optional<PCProductTransaction> findByDestinationProductIdAndName(int bcProductID, String productName);

    public Optional<PCProductTransaction> findByNameAndIsActive(String name, boolean isActive);

    public Optional<PCProductTransaction> findBySkuAndIsActive(String sku, boolean isActive);

    public Optional<PCProductTransaction> findByProductIdAndSkuAndIsActive(int product_id, String itemNumber, boolean isActive);

    public Optional<PCProductTransaction> findBySkuAndIsActiveAndStorehash(String sku, boolean isActive,String storehash);


}
