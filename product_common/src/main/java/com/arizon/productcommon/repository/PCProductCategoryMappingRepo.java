/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductCategoryMapping;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sasikumar.a
 */
public interface PCProductCategoryMappingRepo extends JpaRepository<PCProductCategoryMapping, Integer> {

	List<PCProductCategoryMapping> findByProductIdAndIsActiveAndIsDeleted(int productId, boolean isActive, boolean isDeleted);
	 
    List<PCProductCategoryMapping> findByCategoryIdAndIsActiveAndIsDeleted(int productId, boolean isActive, boolean isDeleted);
    
    Optional<PCProductCategoryMapping> findByProductIdAndCategoryIdAndIsActive(int productId, int categoryId, boolean b);
    
     Optional<PCProductCategoryMapping> findFirstByProductIdAndCategoryId(int productId, int  categoryId);
 
    Optional<PCProductCategoryMapping> findByProductIdAndCategoryId(int productId, int categoryId);
 
}
