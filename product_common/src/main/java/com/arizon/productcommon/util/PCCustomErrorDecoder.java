package com.arizon.productcommon.util;


import com.arizon.productcommon.exception.PCProductFrequentException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;


@Slf4j
public class PCCustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String responsebody = null;
        boolean customException = false;
        String methodName = response.request().httpMethod().name();
        String error = responsebody;
        String errorDetails = null;


        try {
            customException = true;
            if (response.body() != null)
                responsebody = IOUtils.toString(response.body().asInputStream());
            log.info("caught into CustomErrorDecoder-responsebody:" + responsebody);
            log.info("caught into CustomErrorDecoder-url:" + response.request().url());
            log.info("caught into CustomErrorDecoder-methodname:" + methodName);
            log.info("caught into CustomErrorDecoder-responsestatus:" + response.status());
            if (response.status() >= 400 && response.status() <= 499) {
                customException = true;
                error = responsebody;
            }

            errorDetails = error + " [" + response.request().httpMethod().name() + "] " +
                    response.status() + " " + response.request().url();

        } catch (Exception e) {
            log.error("Exception: " + ProductUtility.getStackTrace(e));
            customException = false;
        }

        if (customException)
            return new PCProductFrequentException(errorDetails);
        else
            return FeignException.errorStatus(methodKey, response);
    }
}