package com.arizon.Guidant.restclient;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arizon.Guidant.config.GuidantAwsSecrets;


@Component
@Slf4j
public class GuidantRestClientBuilder {

    @Autowired
    GuidantAwsSecrets guidantAwsSecrets;

    public <T> T createFormSecureClient(Class<T> type, String url) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new FormEncoder())
                .decoder(new GsonDecoder())
               // .errorDecoder(new CustomErrorDecoder())
                .retryer(new Retryer.Default(
                		guidantAwsSecrets.getPeriod(),
                		guidantAwsSecrets.getMax_period(),
                		guidantAwsSecrets.getMax_attempts()))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(type, url);
    }

    public <T> T createSecureClient(Class<T> type, String url) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                //.errorDecoder(new CustomErrorDecoder())
                .retryer(new Retryer.Default(
                		guidantAwsSecrets.getPeriod(),
                		guidantAwsSecrets.getMax_period(),
                		guidantAwsSecrets.getMax_attempts()))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(type, url);
    }
}
