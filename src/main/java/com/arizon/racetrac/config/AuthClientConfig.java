package com.arizon.racetrac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import feign.form.FormEncoder;
import feign.codec.Encoder;
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
