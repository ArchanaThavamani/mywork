	package com.arizon.Guidant.config;
	
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	
	import feign.*;
	import feign.codec.*;
	import feign.form.FormEncoder;
	
	@Configuration
	public class SAPAuthClientConfig {
	
	    @Bean
	    public Logger.Level feignLoggerLevel() {
	        return Logger.Level.FULL;
	    }
	
	    @Bean
	    public Encoder feignFormEncoder() {
	        return new FormEncoder();
	    }
	    
	    
	}
