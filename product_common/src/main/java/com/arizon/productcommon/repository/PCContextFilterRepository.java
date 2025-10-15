package com.arizon.productcommon.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arizon.productcommon.entity.PCContextFilters;

public interface PCContextFilterRepository extends  JpaRepository<PCContextFilters, Integer> {

	Optional<PCContextFilters> findByDestinationFilterId(String destinationFilterId);
	
	List<PCContextFilters> findByDestinationCategoryId(Integer destinationCategoryId);
	
	List<PCContextFilters> findByDestinationCategoryIdIn(List<Integer> destinationcategoryIds);
	
	Optional<PCContextFilters> findByDestinationCategoryIdAndTypeAndDisplayName(Integer destinationCategoryId , String type ,String name);
	
}
