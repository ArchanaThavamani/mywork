/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.restclient;

import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.configs.OCCustomErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author kalaivani.r
 */
@Component("RestClientBuilder")
@Slf4j
public class OCRestClientBuilder {



    public <T> T createSecureClient(Class<T> type, String url) {

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .errorDecoder(new OCCustomErrorDecoder())
                .retryer(new Retryer.Default(Long.parseLong(secrects.getPeriod()), Long.parseLong(secrects.getMax_period()), Integer.parseInt(secrects.getMax_attempts())))
                .options(new Request.Options(Integer.parseInt(secrects.getTimeout_period()), Integer.parseInt(secrects.getTimeout_period())))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .target(type, url);
    }
}
