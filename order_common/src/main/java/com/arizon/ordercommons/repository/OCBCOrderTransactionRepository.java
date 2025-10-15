/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;

import feign.Param;


/**
 *
 * @author kalaivani.r
 */
public interface OCBCOrderTransactionRepository extends JpaRepository<OCBCOrderTableTransaction, Integer> {

    public Optional<OCBCOrderTableTransaction> findByStorehashAndSourceOrderIdAndIsActiveAndIsDeleted(String storehash, int sourceOrderID, boolean isActive, boolean isDelete);

    public Optional<OCBCOrderTableTransaction> findByStorehashAndOrderIdAndIsActiveAndIsDeleted(String storehash, int orderId, boolean isActive, boolean isDeleted);

    public List<OCBCOrderTableTransaction> findByStorehashAndIntegrationStatusIn(String storehash, List<String> status);

    public Optional<OCBCOrderTableTransaction> findByStorehashAndSourceOrderId(String storehash, int sourceOrderID);

    public List<OCBCOrderTableTransaction> findByStorehashAndIntegrationStatus(String storehash, String status);

    @Query(value = "SELECT * FROM orders.tbl_order WHERE integration_status = :param_status And storehash = :storehash AND retry_count <=:retryCount", nativeQuery = true)
    public List<OCBCOrderTableTransaction> getFailedOrder(@Param("param_status") String param_status, @Param("storehash") String storehash, @Param("retryCount") int retryCount);

    public List<OCBCOrderTableTransaction> findAllByStorehashAndOrderIdIn(String storehash, List<Integer> value);

    public List<OCBCOrderTableTransaction> findAllByStorehashAndOrderIdInAndIsActive(String storehash, List<Integer> value, boolean isActive);

}
