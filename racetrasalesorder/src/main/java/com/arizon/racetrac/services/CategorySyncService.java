package com.arizon.racetrac.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arizon.productcommon.controller.PCBCProductsController;

@Service
public class CategorySyncService {

    private static final Logger log = LoggerFactory.getLogger(CategorySyncService.class);

    @Autowired
    private PCBCProductsController bcProductsController;
      //Sync category from BC
    public String syncCategoriesFromBCToTable() {
    try {
        return bcProductsController.oneTimeCategorySync();  // reuse the common logic
    } catch (Exception e) {
        log.error("Error syncing categories from BC to table: {}", e.getMessage(), e);
        return "Failed";
    }
    }         
}
