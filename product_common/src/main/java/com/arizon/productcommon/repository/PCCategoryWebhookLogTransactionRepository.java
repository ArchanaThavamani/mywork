package com.arizon.productcommon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.productcommon.entity.PCCategoryWebhookLogTransaction;

public interface PCCategoryWebhookLogTransactionRepository extends JpaRepository<PCCategoryWebhookLogTransaction, Integer> {

	Optional<PCCategoryWebhookLogTransaction> findByDestinationCategoryIdAndStorehash(Integer bcCategoryId , String storehash);
	List<PCCategoryWebhookLogTransaction> findByStorehashAndStatus(String storehash , String status);
}