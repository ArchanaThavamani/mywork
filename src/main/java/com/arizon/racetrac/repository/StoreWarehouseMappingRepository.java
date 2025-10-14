package com.arizon.racetrac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arizon.racetrac.entity.StoreWarehouseMapping;
import com.google.common.base.Optional;

@Repository
public interface StoreWarehouseMappingRepository extends JpaRepository<StoreWarehouseMapping, Integer> {
    
    Optional<StoreWarehouseMapping> findByStoreId(Integer storeId);
}

