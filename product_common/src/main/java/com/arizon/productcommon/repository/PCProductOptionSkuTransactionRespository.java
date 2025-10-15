/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.repository;

import com.arizon.productcommon.entity.PCProductOptionSkuTransaction;
import feign.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kavinkumar.s
 */
public interface PCProductOptionSkuTransactionRespository extends JpaRepository<PCProductOptionSkuTransaction, Integer> {

    List<PCProductOptionSkuTransaction> findByproductID(int productID);

    Optional<PCProductOptionSkuTransaction> findByproductIDAndSku(Integer productId, String productSku);

    Optional<PCProductOptionSkuTransaction> findBySkuAndIsActive(String sku, boolean isActive);

    Optional<PCProductOptionSkuTransaction> findBySkuAndProductIDAndIsActive(String sku, int product_id, boolean isActive);

    public List<PCProductOptionSkuTransaction> findByProductIDAndIsActive(int product_id, boolean isActive);

    public List<PCProductOptionSkuTransaction> findByProductIDAndStatus(Integer productId, String status);

    Optional<PCProductOptionSkuTransaction> findByProductOptionSkuID(int product_option_sku_id);

    Optional<PCProductOptionSkuTransaction> findByProductOptionSkuIDAndProductID(int product_option_sku_id, int product_id);

    Optional<PCProductOptionSkuTransaction> findByProductOptionSkuIDAndIsActiveAndIsDeleted(int product_option_sku_id, boolean isActive, boolean isDeleted);

    public List<PCProductOptionSkuTransaction> findByProductIDAndIsDeleted(int product_id, boolean b);

    public List<PCProductOptionSkuTransaction> findByProductIDAndIsActiveAndIsDeleted(int check_option_product_id, boolean isActive, boolean isDeleted);

    public Optional<PCProductOptionSkuTransaction> findByProductIDAndDestinationSkuIDAndIsActiveAndIsDeleted(Integer productId, int sku_id, boolean b, boolean b0);

}
