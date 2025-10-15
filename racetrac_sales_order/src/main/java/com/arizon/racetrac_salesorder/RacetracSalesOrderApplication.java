package com.arizon.racetrac_salesorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages ="com.arizon.ordercommons")
@EnableAutoConfiguration
@EntityScan
@ComponentScan(basePackages = "com.arizon.racetrac_salesorder")

public class RacetracSalesOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(RacetracSalesOrderApplication.class, args);
	}

}
