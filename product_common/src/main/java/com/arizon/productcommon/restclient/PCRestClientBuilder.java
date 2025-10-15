/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.restclient;

import static com.arizon.productcommon.config.PCConstants.secrets;
import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.util.PCCustomErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author mohan.e
 */
@Component
@Slf4j
public class PCRestClientBuilder {

    @Autowired
    PCProductCommonConfig commonconfig;
    
    public <T> T createSecureClientFormBuilder(Class<T> type, String url) {

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .errorDecoder(new PCCustomErrorDecoder())
                .retryer(new Retryer.Default(Long.parseLong(secrets.getPeriod()), Long.parseLong(secrets.getMax_period()), Integer.parseInt(secrets.getMax_attempts())))
                .options(new Request.Options(Integer.parseInt(secrets.getTimeout_period()), Integer.parseInt(secrets.getTimeout_period())))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(type, url);
    }

    public <T> T createSecureClientBuilder(Class<T> type, String url) {

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new PCCustomErrorDecoder())
                .retryer(new Retryer.Default(Long.parseLong(secrets.getPeriod()), Long.parseLong(secrets.getMax_period()), Integer.parseInt(secrets.getMax_attempts())))
                .options(new Request.Options(Integer.parseInt(secrets.getTimeout_period()), Integer.parseInt(secrets.getTimeout_period())))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(type, url);
    }

//    public <T> T createSecureClientFormBuilder(Class<T> type, String url) {
//        return Feign.builder()
//                .client(new OkHttpClient())
//                .encoder(new GsonEncoder())
//                .decoder(new GsonDecoder())
//                .errorDecoder(new CustomErrorDecoder())
//                .retryer(new Retryer.Default(Long.parseLong(commonconfig.getPeriod()),Long.parseLong(commonconfig.getMaxPeriod()),Integer.parseInt(commonconfig.getMaxAttempts())))
//                .logLevel(Logger.Level.FULL)
//                .logger(new Slf4jLogger())
//                .target(type, url);
//    }
    
}
