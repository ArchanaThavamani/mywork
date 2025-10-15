package com.arizon.racetrac_salesorder.conig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.codec.Encoder;
import feign.form.FormEncoder;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;

@Configuration
public class AuthClientConfig {
	
	 @Bean
	    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
	        return new FormEncoder(new SpringEncoder(messageConverters));
	    }

}
