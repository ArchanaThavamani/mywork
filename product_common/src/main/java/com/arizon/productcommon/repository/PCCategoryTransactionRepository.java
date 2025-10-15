/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCCategoryTransaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mohan.e
 */
public interface PCCategoryTransactionRepository extends JpaRepository<PCCategoryTransaction, Integer> {

    List<PCCategoryTransaction> findByStatusAndStorehash(String status, String storehash);

    List<PCCategoryTransaction> findByStatusAndStorehashAndParentCategoryIdIsNull(String status, String storehash);

    List<PCCategoryTransaction> findByStatusAndStorehashAndParentCategoryId(String status, String storehash, Integer parentCategoryId);

    Optional<PCCategoryTransaction> findByCategoryId(int categoryId);
    
    Optional <PCCategoryTransaction> findByCategoryIdAndStorehash(int categoryId , String storehash);

    Optional<PCCategoryTransaction> findByStorehashAndDestinationCategoryId(String storehash, Integer id);

    Optional<PCCategoryTransaction> findFirstByDestinationCategoryIdAndStorehash(Integer id, String storehash);

    List<PCCategoryTransaction> findByDestinationParentCategoryIdAndStorehashAndParentCategoryIdIsNull(Integer id, String storehash);

    Optional<PCCategoryTransaction> findFirstByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(Integer id, String storehash, boolean isActive, boolean isDeleted);

    Optional<PCCategoryTransaction> findBysourceCategoryIdAndIsActiveAndIsDeleted(Integer sourceCategoryId, boolean isActive, boolean isDeleted);

  //  Optional<PCCategoryTransaction> findByNameAndIsActiveAndIsDeleted(String name, boolean isActive, boolean isDeleted);
    List<PCCategoryTransaction> findByNameAndIsActiveAndIsDeleted(String name, boolean isActive, boolean isDeleted);

    Optional<PCCategoryTransaction> findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(int category_id, String storeHash ,boolean isActive, boolean isDeleted);


}
