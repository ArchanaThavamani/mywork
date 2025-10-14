package com.arizon.racetrac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arizon.racetrac.services.RacetracInventoryService;

@RestController
public class RacetracInventoryController {

    @Autowired
    RacetracInventoryService racetracInventoryService;

    @GetMapping("/integrateInventory")
    public void integrateInventorytoBC() {
        racetracInventoryService.integrateInventoryDetails();
    }

}
