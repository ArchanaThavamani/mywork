package com.arizon.racetrac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.arizon.racetrac",
    "com.arizon.productcommon",
    "com.arizon.ordercommons"
})
@EnableJpaRepositories(basePackages = {
    "com.arizon.racetrac",
    "com.arizon.productcommon",
    "com.arizon.ordercommons.repository"
})
@EntityScan(basePackages = {
    "com.arizon.racetrac",
    "com.arizon.productcommon",
    "com.arizon.ordercommons.entity"
})
@EnableFeignClients(basePackages = {
    "com.arizon.racetrac", 
    "com.arizon.productcommon",
    "com.arizon.ordercommons"
})
public class RaceTracApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaceTracApplication.class, args);
    }

}
