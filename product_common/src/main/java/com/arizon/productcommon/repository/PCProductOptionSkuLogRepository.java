/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionSkuLogTransaction;
import feign.Param;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kavinkumar.s
 */
public interface PCProductOptionSkuLogRepository extends JpaRepository<PCProductOptionSkuLogTransaction, Integer> {

    @Query(value = "select * from product.tbl_product_option_sku_ds_log where created_date >=:param_created_date", nativeQuery = true)
    public List<PCProductOptionSkuLogTransaction> getProductLogDetails(@Param("param_created_date") Timestamp param_created_date);
}
