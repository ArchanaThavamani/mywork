package com.arizon.Guidant;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;



@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.arizon.*")
@EnableJpaRepositories(basePackages = "com.arizon.*")
@EntityScan(basePackages = "com.arizon.*")
@Slf4j
@Service
@EnableFeignClients(basePackages = "com.arizon.Guidant.restclient")
@Configuration
public class GuidantApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuidantApplication.class, args);
	}
	
	
}
