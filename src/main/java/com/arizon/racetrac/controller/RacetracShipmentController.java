package com.arizon.racetrac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arizon.racetrac.services.RacetracShipmentService;


@RestController
public class RacetracShipmentController {

    @Autowired
    RacetracShipmentService racetracShipmentService;    

    @GetMapping("/integrateShipmentRead")
    public void testShipmentRead() {
        racetracShipmentService.integrateShipmentsDetails();
    }

@GetMapping("/ordercompleted")
public void orderCompletedStatus(){
    racetracShipmentService.markOrderCompleted("cr0kczybyx", 118);
}
}
