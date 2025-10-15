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

import com.arizon.ordercommons.entity.OCBCOrderWebhookTableTransaction;

import feign.Param;

/**
 *
 * @author kalaivani.r
 */
public interface OCBCOrderWebhookTransactionRepository extends JpaRepository<OCBCOrderWebhookTableTransaction, Integer> {

    public List<OCBCOrderWebhookTableTransaction> findByStatusAndStorehash(String WebhookPendingStatus, String storehash);

    public Optional<OCBCOrderWebhookTableTransaction> findByDestinationOrderIdAndStorehash(int BCOrderID, String storehash);

    @Query(value = "SELECT * FROM orders.tbl_order_webhook_log WHERE status = :param_status And storehash = :storehash AND retry_count = 1", nativeQuery = true)
    public List<OCBCOrderWebhookTableTransaction> getFailedWebhookDetails(@Param("param_status") String param_status, @Param("storehash") String storehash);
}
