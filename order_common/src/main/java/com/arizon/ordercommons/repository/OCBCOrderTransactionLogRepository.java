/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCBCOrderTransactionLog;
import feign.Param;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kalaivani.r
 */
public interface OCBCOrderTransactionLogRepository extends JpaRepository<OCBCOrderTransactionLog, Integer> {

    public List<OCBCOrderTransactionLog> findByCreatedAtBetween(Timestamp starttime, Timestamp endtime);

    @Query(value = "SELECT * FROM orders.tbl_order_ds_log WHERE created_date >= :param_created_date", nativeQuery = true)
    public List<OCBCOrderTransactionLog> getCustomerLogDetails(@Param("param_created_date") Timestamp param_created_date);
}
