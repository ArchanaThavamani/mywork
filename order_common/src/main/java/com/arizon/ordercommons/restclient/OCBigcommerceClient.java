/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.restclient;

import com.arizon.ordercommons.model.BCOrderShipmentsDTO;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.math.BigDecimal;

/**
 *
 * @author kavinkumar.s
 */
public interface OCBigcommerceClient {

    @RequestLine(value = "GET /{bcStoreHash}/v2/orders/{order_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "X-Auth-Token: {accessToken}"
    })
    public feign.Response getOrderById(@Param("order_id") int order_id, @Param("accessToken") String accessToken, @Param("bcStoreHash") String storeHash);

    @RequestLine(value = "GET /{bcStoreHash}/v2/orders/{order_id}/products?limit=250")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"

    })
    public feign.Response getOrderProductDetails(@Param("order_id") int order_id, @Param("accessToken") String access_token, @Param("bcStoreHash") String storeHash);

    @RequestLine(value = "GET /{bcStoreHash}/v2/orders/{order_id}/shipping_addresses")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"

    })
    public feign.Response getOrderShippingAddress(@Param("order_id") int order_id, @Param("accessToken") String access_token, @Param("bcStoreHash") String storeHash);

    @RequestLine(value = "GET /{bcStoreHash}/v2/orders/{order_id}/shipments")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"

    })
    public BCOrderShipmentsDTO getOrderShipments(@Param("order_id") int order_id, @Param("accessToken") String access_token, @Param("bcStoreHash") String storeHash);

    @RequestLine(value = "GET /{bcStoreHash}/v3/hooks/{webhook_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"

    })
    public feign.Response getWebhook(@Param("webhook_id") BigDecimal webhook_id, @Param("accessToken") String access_token, @Param("bcStoreHash") String bcStoreHash);

    @RequestLine(value = "PUT /{bcStoreHash}/v3/hooks/{webhook_id}")
    @Body("{jsonRequest}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"
    })
    public feign.Response updateWebhook(@Param("jsonRequest") String jsonRequest, @Param("webhook_id") BigDecimal webhook_id, @Param("accessToken") String access_token, @Param("bcStoreHash") String bcStoreHash);

    @RequestLine(value = "GET /{bcStoreHash}/v2/orders?min_date_modified={min_modified_date}&page={pageCount}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"
    })
    public feign.Response getOrderByModifiedTime(@Param("accessToken") String accessToken, @Param("bcStoreHash") String bcStoreHash, @Param("min_modified_date") String min_modified_data, @Param("pageCount") Integer pageCount);

    @RequestLine(value = "PUT /{bcStoreHash}/v2/orders/{order_id}")
    @Body("{jsonRequest}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"
    })
    public feign.Response updateOrder(@Param("accessToken") String accessToken, @Param("bcStoreHash") String bcStoreHash, @Param("order_id") Integer order_id, @Param("jsonRequest") String jsonRequest);

    @RequestLine(value = "GET /{bcStoreHash}/v3/orders/{order_id}/transactions")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {accessToken}"
    })
    public feign.Response getPaymentMethodName(@Param("accessToken") String accessToken, @Param("bcStoreHash") String bcStoreHash, @Param("order_id") Integer order_id);
    
}
