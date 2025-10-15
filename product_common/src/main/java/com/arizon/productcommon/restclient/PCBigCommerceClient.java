/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.restclient;

import com.arizon.productcommon.exception.PCProductFrequentException;
import com.arizon.productcommon.model.CustomFeildGetResponseDTO;
import com.arizon.productcommon.model.CustomFeildResponseDTO;
import com.arizon.productcommon.model.ProductDTO;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 *
 * @author mohan.e
 */
public interface PCBigCommerceClient {

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/products/{product_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "X-Auth-Token: {AccessToken}"
    })
    public feign.Response deleteProduct(@Param("product_id") int product_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/products")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "X-Auth-Token: {AccessToken}"
    })
    public feign.Response createProduct(ProductDTO productRequest, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/categories")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{categoryRequest}")
    public feign.Response createCategory(@Param("categoryRequest") String categoryRequest, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/categories/{category_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{categoryRequest}")
    public feign.Response updateCategory(@Param("categoryRequest") String categoryRequest, @Param("category_id") int category_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/categories/{category_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteCategory(@Param("category_id") int category_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "GET/{store_hash}/v3/catalog/products/{product_id}/custom-fields")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public CustomFeildGetResponseDTO getCustomFeilds(@Param("product_id") int product_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/products/{product_id}/custom-fields")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{customFeildRequest}")
    public feign.Response createCustomFeilds(@Param("customFeildRequest") String customFeildRequest, @Param("product_id") int product_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken) throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/products/{product_id}/custom-fields/{custom_field_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{customFeildRequest}")
    public feign.Response updateCustomFeilds(@Param("customFeildRequest") String customFeildRequest, @Param("product_id") int product_id, @Param("custom_field_id") int custom_field_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/products/{product_id}/custom-fields/{custom_field_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteCustomField(@Param("product_id") int product_id, @Param("custom_field_id") int custom_field_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/brands")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{brandRequest}")
    public feign.Response createBrand(@Param("brandRequest") String brandRequest, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;
    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/brands/{brandId}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteBrand(@Param("brandId") int brandId, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/products/{product_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{updateProductRequest}")
    public feign.Response updateProduct(@Param("updateProductRequest") String updateProductRequest, @Param("product_id") int product_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/products/{product_id}/options")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{optionData}")
    public feign.Response createProductOption(@Param("optionData") String optionData, @Param("product_id") int product_id, @Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken)  throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/products/{product_id}/options/{option_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{optionData}")
    public feign.Response updateProductOption(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("optionData") String optionData, @Param("product_id") int product_id, @Param("option_id") int optionID)  throws PCProductFrequentException;

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/products/{product_id}/options/{option_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteProductOption(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("product_id") int product_id, @Param("option_id") int optionID)  throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/products/{product_id}/options/{option_id}/values")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{optionvalue}")
    public feign.Response createProductOptionValue(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("optionvalue") String optionvalue, @Param("product_id") int product_id, @Param("option_id") int option_id)  throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/products/{product_id}/options/{option_id}/values/{value_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{optionvalue}")
    public feign.Response updateProductOptionValue(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("optionvalue") String optionvalue, @Param("product_id") int product_id, @Param("option_id") int option_id, @Param("value_id") int valueID)  throws PCProductFrequentException;

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/products/{product_id}/options/{option_id}/values/{value_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteProductOptionValue(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("product_id") int product_id, @Param("option_id") int option_id, @Param("value_id") int valueID)  throws PCProductFrequentException;

    @RequestLine(value = "POST/{store_hash}/v3/catalog/products/{product_id}/variants")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{productSkuRequest}")
    public feign.Response createProductVariant(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("productSkuRequest") String productSkuRequest, @Param("product_id") int product_id) throws PCProductFrequentException;

    @RequestLine(value = "PUT/{store_hash}/v3/catalog/products/{product_id}/variants/{variant_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    @Body("{productSkuRequest}")
    public feign.Response updateProductVariant(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("productSkuRequest") String productSkuRequest, @Param("product_id") int product_id, @Param("variant_id") int variant_id)  throws PCProductFrequentException;

    @RequestLine(value = "DELETE/{store_hash}/v3/catalog/products/{product_id}/variants/{variant_id}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response deleteProductVariant(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("product_id") int product_id, @Param("variant_id") int variant_id)  throws PCProductFrequentException;

    @RequestLine(value = "GET /{store_hash}/v3/catalog/brands?limit=250&page={page}")
    @Headers({
            "accept: application/json",
            "Content-Type: application/json",
            "x-auth-token: {AccessToken}"
    })
    public feign.Response getBrandAll(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("page") int page)  throws PCProductFrequentException;


    @RequestLine(value = "GET /{store_hash}/v3/catalog/categories?limit=250&page={page}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getCategoryAll(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("page") int page)  throws PCProductFrequentException;

    @RequestLine(value = "GET /{store_hash}/v3/catalog/products?include={includeFields}&limit=250&page={page}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })

    public feign.Response getProductAll(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken,@Param("includeFields") String includeFields ,@Param("page") int page)  throws PCProductFrequentException;

    @RequestLine(value = "GET /{store_hash}/v3/catalog/products/{product_id}?include={includeFields}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getProductById(@Param("store_hash") String store_hash,
                                         @Param("AccessToken") String AccessToken,
                                         @Param("product_id") int productId,
                                         @Param("includeFields") String includeFields)
            throws PCProductFrequentException;
    
    @RequestLine(value = "GET /{store_hash}/v3/catalog/trees/categories?tree_id:in={tree_id}&limit=250&page={page}")
    @Headers({
        "accept: application/json",
        "Content-Type: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getCategoryByTreeID(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("page") int page ,@Param("tree_id") int tree_id);

    @RequestLine(value = "GET /{store_hash}/v3/catalog/trees/categories?category_id:in={category_id}&page={page}")
    @Headers({
        "accept: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getCategoryByID(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken, @Param("page") int page ,@Param("category_id") int category_id);

    @RequestLine(value = "GET /{store_hash}/v3/settings/search/filters/contexts")
    @Headers({
        "accept: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getAllContextFilters(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken);
    
    @RequestLine(value = "GET /{store_hash}/v3/settings/search/filters/contexts?category_id:in:{category_id}")
    @Headers({
        "accept: application/json",
        "x-auth-token: {AccessToken}"
    })
    public feign.Response getContextFiltersByCategoryId(@Param("store_hash") String store_hash, @Param("AccessToken") String AccessToken,@Param("category_id") int category_id);
       
}
