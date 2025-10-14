package com.arizon.racetrac.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_warehouse_mapping_id", schema = "commonbc")
public class StoreWarehouseMapping {

    @Id
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "store_email")
    private String storeEmail;

    @Column(name = "storehash")
    private String storeHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "city")
    private String city;

    // Getters and Setters

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public String getStoreHash() {
        return storeHash;
    }

    public void setStoreHash(String storeHash) {
        this.storeHash = storeHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

