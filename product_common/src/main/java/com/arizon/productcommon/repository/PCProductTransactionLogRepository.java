/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductTransactionLog;
import feign.Param;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author mohan.e
 */
public interface PCProductTransactionLogRepository extends JpaRepository<PCProductTransactionLog, Integer> {

//    @Query(value = "SELECT * FROM product.tbl_product_ds_log WHERE modified_date>=:param_created_date", nativeQuery = true)
    @Query(value = "select * from (select *, RANK() OVER (PARTITION BY product_id ORDER BY modified_date DESC)as RN from product.tbl_product_ds_log where created_date >=:param_created_date)A where RN = 1", nativeQuery = true)
    public List<PCProductTransactionLog> getProductLogDetails(@Param("param_created_date") Timestamp param_created_date);
}
