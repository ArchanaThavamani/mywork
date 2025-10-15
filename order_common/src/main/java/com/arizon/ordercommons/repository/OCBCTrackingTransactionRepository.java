/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.repository;

import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;
import com.arizon.ordercommons.entity.OCBCStateCountryCodeTransaction;
import com.arizon.ordercommons.entity.OCBCTrackingTableTransaction;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



public interface OCBCTrackingTransactionRepository extends JpaRepository<OCBCTrackingTableTransaction, Integer> {

    public OCBCTrackingTableTransaction findFirstByTypeAndStorehashAndOrderIdAndStatus(String type, String storeHash, String orderId, String status);

    @Query(value = "select * from commonbc.tbl_tracking where created_date >=  (current_date - :days) and storehash = :storehash", nativeQuery = true)
    public List<OCBCTrackingTableTransaction> findByOrderByStorehashAndDate(@Param("days") Integer days, @Param("storehash") String storehash);
}
