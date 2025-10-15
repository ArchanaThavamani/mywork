/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCCategoryTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author mohan.e
 */
public interface PCCategoryTransactionLogRepository extends JpaRepository<PCCategoryTransactionLog,Integer>{
    
}
