package com.arizon.productcommon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.arizon.productcommon.*")
@EnableJpaRepositories("com.arizon.productcommon.*")
@EntityScan("com.arizon.productcommon.*")
@Slf4j
@Configuration
public class ProductCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCommonApplication.class, args);
    }

}
