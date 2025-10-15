package com.arizon.productcommon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arizon.productcommon.entity.PCProductWebhookLogTransaction;

@Repository
public interface PCProductWebhookLogTransactionRepository extends JpaRepository<PCProductWebhookLogTransaction, Integer> {

	Optional<PCProductWebhookLogTransaction> findByDestinationProductIdAndStorehash(Integer destinationProductId , String Stroehash);
	List<PCProductWebhookLogTransaction> findByStorehashAndStatus(String storehash , String status);
}
