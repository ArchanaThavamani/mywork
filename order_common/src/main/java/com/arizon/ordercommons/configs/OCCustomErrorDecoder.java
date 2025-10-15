/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.configs;

import com.arizon.ordercommons.util.OrderUtility;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author kavinkumar.s
 */
@Slf4j
public class OCCustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String responsebody = null;
        boolean customException = false;
        String methodName = response.request().httpMethod().name();
        String error = responsebody;
        String errorDetails = null;

        try {
            customException = true;
            responsebody = IOUtils.toString(response.body().asInputStream());

            log.info("caught into CustomErrorDecoder-responsebody:" + responsebody);
            log.info("caught into CustomErrorDecoder-url:" + response.request().url());
            log.info("caught into CustomErrorDecoder-methodname:" + methodName);
            log.info("caught into CustomErrorDecoder-responsestatus:" + response.status());

            if (response.status() >= 400 && response.status() <= 499) {
                customException = true;
                error = responsebody;
            }

            errorDetails = error + " [" + response.request().httpMethod().name() + "] "
                    + response.status() + " " + response.request().url();

        } catch (Exception e) {
            log.error("Exception: " + OrderUtility.getStackTrace(e));
            customException = false;
        }

        if (customException) {
            return null;
        } else {
            return FeignException.errorStatus(methodKey, response);
        }
    }
}
