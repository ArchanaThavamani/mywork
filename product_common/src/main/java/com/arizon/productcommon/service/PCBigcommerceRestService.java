/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.service;

import com.arizon.productcommon.config.PCConstants;

import static com.arizon.productcommon.config.PCConstants.FAILED;
import static com.arizon.productcommon.config.PCConstants.INPROGRESS;
import static com.arizon.productcommon.config.PCConstants.SUCCESS;
import static com.arizon.productcommon.config.PCConstants.ZEROS;
import static com.arizon.productcommon.config.PCConstants.secrets;

import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.entity.PCBrandTransaction;
import com.arizon.productcommon.entity.PCCategoryTransaction;
import com.arizon.productcommon.entity.PCProductCustomFieldTransaction;
import com.arizon.productcommon.entity.PCProductWebhookLogTransaction;
import com.arizon.productcommon.exception.PCProductFrequentException;
import com.arizon.productcommon.model.*;
import com.arizon.productcommon.repository.PCBrandTransactionRepository;
import com.arizon.productcommon.repository.PCProductWebhookLogTransactionRepository;
import com.arizon.productcommon.restclient.PCBigCommerceClient;
import com.arizon.productcommon.restclient.PCRestClientBuilder;
import com.arizon.productcommon.util.ProductUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sasikumar.a
 */
@Service
@Slf4j
public class PCBigcommerceRestService {

    @Autowired
    PCRestClientBuilder PCRestClientBuilder;

    @Autowired
    PCAdisLogService PCAdisLogService;

    @Autowired
    PCBrandTransactionRepository pcBrandTransactionRepository;

    @Autowired
    PCProductEmailService emailService;

    @Autowired
    PCProductCommonConfig config;
    
    @Autowired
    PCProductWebhookLogTransactionRepository productWebhookRepo;

    public int CreateProductInBc(ProductDTO productDTO, String ProductRequest, int productId) {
        try {
            int bcProductId = 0;
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient BcClient = PCRestClientBuilder.createSecureClientBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = BcClient.createProduct(productDTO, secrets.getStorehash(), secrets.getAccessToken());
            String bcresponse = null;
            if (response != null & response.body() != null) {
                bcresponse = IOUtils.toString(response.body().asReader());
            }
            log.info("response status from Big commerce :" + response);
            if (response.status() == 200) {
                ProductResponseDTO productresponse = mapper.readValue(bcresponse, new TypeReference<ProductResponseDTO>() {
                });
                bcProductId = productresponse.getData().getId();
                PCAdisLogService.productStatusUpdate(productId, INPROGRESS, productresponse.getData());
                PCAdisLogService.insertProductLog(productId, SUCCESS, PCConstants.CREATE, ProductRequest, bcresponse, bcProductId);
                return bcProductId;
            } else {
                PCAdisLogService.productStatusUpdate(productId, FAILED, null);
                PCAdisLogService.insertProductLog(productId, FAILED, PCConstants.CREATE, ProductRequest, bcresponse, PCConstants.ZEROS);
                return bcProductId;
            }
        } catch (Exception Ex) {
            log.error("Exception -> BigcommerceRestService : CreateProductInBc " + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CreateProductInBc", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.ZEROS;
        }
    }

    public String createCategoryInBc(String categoryData, PCCategoryTransaction category) {
        try {
            CategoryResponseDTO CategoryResponse = null;
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.createCategory(categoryData, secrets.getStorehash(), secrets.getAccessToken());
            int bcStatus = response.status();

            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }
            if (bcStatus == 200) {

                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                CategoryResponse = mapper.readValue(bcResponse, CategoryResponseDTO.class);
                Integer destinstionCategoryID = CategoryResponse.getData().get(0).getId();
                Integer parentCategoryId = CategoryResponse.getData().get(0).getParent_id();
                PCAdisLogService.CategoryToAdis(CategoryResponse, category, PCConstants.SUCCESS);
                PCAdisLogService.storeCategoryLog(category, null, PCConstants.SUCCESS, PCConstants.CREATE, null);
            } else {
                log.info("Category doesn't create in big commerce of this category : " + bcResponse);
                PCAdisLogService.CategoryToAdis(CategoryResponse, category, PCConstants.FAILED);
                PCAdisLogService.storeCategoryLog(category, bcResponse, PCConstants.FAILED, PCConstants.CREATE, categoryData);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : createCategoryInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createCategoryInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String updateCategoryInBc(String categoryData, PCCategoryTransaction category) {
        try {
            CategoryResponseDTO CategoryResponse = null;
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.updateCategory(categoryData, category.getDestinationCategoryId(), secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }
            if (response.status() == 200) {
                mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                CategoryResponse = mapper.readValue(bcResponse, CategoryResponseDTO.class);

                PCAdisLogService.CategoryToAdis(CategoryResponse, category, PCConstants.SUCCESS);
                PCAdisLogService.storeCategoryLog(category, null, PCConstants.SUCCESS, PCConstants.UPDATE, null);
            } else {
                log.info("Category doesn't create in big commerce of this product : " + bcResponse);
                PCAdisLogService.CategoryToAdis(CategoryResponse, category, PCConstants.FAILED);
                PCAdisLogService.storeCategoryLog(category, bcResponse, PCConstants.FAILED, PCConstants.UPDATE, categoryData);
            }

            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : updateCategoryInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateCategoryInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public void deleteCategoryInBc(PCCategoryTransaction category) {
        try {
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.deleteCategory(category.getDestinationCategoryId(), secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null && response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 204) {
                PCAdisLogService.CategoryToAdis(null, category, PCConstants.DELETE);
                PCAdisLogService.storeCategoryLog(category, null, PCConstants.SUCCESS, PCConstants.DELETE, null);
            } else {
                PCAdisLogService.CategoryToAdis(null, category, PCConstants.FAILED);
                PCAdisLogService.storeCategoryLog(category, bcResponse, PCConstants.FAILED, PCConstants.DELETE, null);
            }
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : deleteCategoryInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteCategoryInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public String updateProductCategoryInBC(String categoryRequest, int desProductId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = bcClientBuilder.updateProduct(categoryRequest, desProductId, secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }
            log.info("response status from Big commerce :" + response.status() + " - BC Product id : " + desProductId);

            if (response.status() == 200) {
                ProductResponseDTO productresponse = mapper.readValue(bcResponse, new TypeReference<ProductResponseDTO>() {
                });

                int bcProductId = productresponse.getData().getId();
//                log.info("updated category in Bc : " + productresponse.getData().getCategories() + " product id : " + bcProductId);
//                adisLogService.productStatusUpdate(bcProductId, SUCCESS, productresponse.getData());
//                adisLogService.insertProductLog(bcProductId, SUCCESS, UPDATE, ProductRequest.toString(), bcResponse);

            } else {
                log.info("updating the category for product in Bc get failed : " + bcResponse);
//                adisLogService.productStatusUpdate(ZERO, FAILED, productresponse.getData());
//                adisLogService.insertProductLog(ZERO, FAILED, UPDATE, ProductRequest.toString(), bcResponse);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : updateProductCategoryInBC " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateProductCategoryInBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public Integer createBrandInBc(String brandRequest, PCBrandTransaction brand, int AdisBrandId) {
        try {
            int bcBrandId = 0;
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.createBrand(brandRequest, secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 200) {
                BrandResponseDTO brandresponse = mapper.readValue(bcResponse, new TypeReference<BrandResponseDTO>() {
                });
                bcBrandId = brandresponse.getData().get(0).getId();
                PCAdisLogService.BrandToAdis(bcBrandId, brandresponse.getData().get(0), PCConstants.SUCCESS, brand);
                PCAdisLogService.BrandToAdisLog(AdisBrandId, bcBrandId, PCConstants.DELETE, SUCCESS, null, null);

            } else {
                log.info("Brand doesn't create while facing issue in big commerce" + bcResponse);
                PCAdisLogService.BrandToAdis(bcBrandId, null, PCConstants.FAILED, brand);
                PCAdisLogService.BrandToAdisLog(AdisBrandId, bcBrandId, PCConstants.DELETE, FAILED, brandRequest, bcResponse);

            }
            return bcBrandId;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : createBrandInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createBrandInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return ZEROS;
        }
    }

    public void deleteBrandInBc(int bcBrandId, PCBrandTransaction brand, int AdisBrandId) {
        try {
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.deleteBrand(bcBrandId, secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 204) {
                brand.setStatus(PCConstants.SUCCESS);
                pcBrandTransactionRepository.save(brand);
                log.info("Brand has been deleted in bigcommerce successfully : " + bcBrandId);
                PCAdisLogService.BrandToAdis(bcBrandId, null, PCConstants.DELETE, brand);
                PCAdisLogService.BrandToAdisLog(AdisBrandId, bcBrandId, PCConstants.DELETE, SUCCESS, null, null);
            } else {
                PCAdisLogService.BrandToAdis(bcBrandId, null, PCConstants.FAILED, brand);
                PCAdisLogService.BrandToAdisLog(AdisBrandId, bcBrandId, PCConstants.DELETE, FAILED, null, bcResponse);
            }
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : deleteBrandInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteBrandInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public String updateProductInBc(String updateProductRequest, int bcProductId, int productId) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response BcResponse = RestClient.updateProduct(updateProductRequest, bcProductId, secrets.getStorehash(), secrets.getAccessToken());
            String bcProductResponse = null;
            if (BcResponse != null & BcResponse.body() != null) {
                bcProductResponse = IOUtils.toString(BcResponse.body().asReader());
            }

            if (BcResponse.status() == 200) {
                ProductResponseDTO productresponse = mapper.readValue(bcProductResponse, new TypeReference<ProductResponseDTO>() {
                });
                log.info("The product is updated in Bigcommerce. Id : " + bcProductId);
                PCAdisLogService.productStatusUpdate(productId, INPROGRESS, productresponse.getData());
                PCAdisLogService.insertProductLog(productId, SUCCESS, PCConstants.UPDATE, null, null, bcProductId);
            } else {
                log.info("The product is not updating properly due to some issues in BigCommerce. " + bcProductResponse);
                PCAdisLogService.productStatusUpdate(productId, FAILED, null);
                PCAdisLogService.insertProductLog(productId, FAILED, PCConstants.CREATE, updateProductRequest, bcProductResponse, ZEROS);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : updateProductInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateProductInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String deleteProductInBc(int bcProductId, int productId) {
        try {
            PCBigCommerceClient BcClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = BcClient.deleteProduct(bcProductId, secrets.getStorehash(), secrets.getAccessToken());
            String bcProductResponse = null;
            if (response != null & response.body() != null) {
                bcProductResponse = IOUtils.toString(response.body().asReader());
            }

            int bcStatus = response.status();
            log.info("BigCommerceProductService.productDelete method Started");
            if (bcStatus == 204) {
                PCAdisLogService.productStatusUpdate(productId, PCConstants.DELETE, null);
                PCAdisLogService.insertProductLog(productId, SUCCESS, PCConstants.DELETE, null, null, bcProductId);
            } else {
                PCAdisLogService.productStatusUpdate(productId, PCConstants.FAILED, null);
                PCAdisLogService.insertProductLog(productId, FAILED, PCConstants.DELETE, null, bcProductResponse, bcProductId);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : deleteProductInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteProductInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String CreateCustomFieldInBc(String customRequest, int bcProductId, PCProductCustomFieldTransaction customData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.createCustomFeilds(customRequest, bcProductId, secrets.getStorehash(), secrets.getAccessToken());

            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 200) {
                CustomFeildResponseDTO customresponse = mapper.readValue(bcResponse, new TypeReference<CustomFeildResponseDTO>() {
                });
                int customeFeildId = customresponse.getData().getId();
                PCAdisLogService.CustomFeildToAdis(PCConstants.SUCCESS, customData, bcProductId, customresponse.getData().getId());
                PCAdisLogService.CustomFeildToAdisLog(customresponse.getData().getName(), SUCCESS, PCConstants.CREATE, null, null, customData.getProductCustomFieldsId(), customeFeildId);
            } else {
                log.info("creating custom field for product gets : " + response.status() + "" + bcResponse);
                PCAdisLogService.CustomFeildToAdis(PCConstants.FAILED, customData, bcProductId, ZEROS);
                PCAdisLogService.CustomFeildToAdisLog(customData.getName(), FAILED, PCConstants.CREATE, customRequest, bcResponse, customData.getProductCustomFieldsId(), ZEROS);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : CreateCustomFieldInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "CreateCustomFieldInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String updateCustomFieldInBc(String customRequest, int bcProductId, int bcCustomId, PCProductCustomFieldTransaction customData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.updateCustomFeilds(customRequest, bcProductId, bcCustomId, secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 200) {
                CustomFeildResponseDTO customresponse = mapper.readValue(bcResponse, new TypeReference<CustomFeildResponseDTO>() {
                });
                int customeFeildId = customresponse.getData().getId();
                PCAdisLogService.CustomFeildToAdis(PCConstants.SUCCESS, customData, bcProductId, customeFeildId);
                PCAdisLogService.CustomFeildToAdisLog(customresponse.getData().getName(), SUCCESS, PCConstants.UPDATE, null, null, customData.getProductCustomFieldsId(), customeFeildId);
            } else {
                log.info("creating custom field for product gets : " + response.status() + " " + bcResponse);
                PCAdisLogService.CustomFeildToAdis(PCConstants.FAILED, customData, bcProductId, customData.getDestinationProductCustomFieldsId());
                PCAdisLogService.CustomFeildToAdisLog(customData.getName(), FAILED, PCConstants.UPDATE, customRequest, bcResponse, customData.getProductCustomFieldsId(), customData.getDestinationProductCustomFieldsId());
            }

            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : updateCustomFieldInBc " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateCustomFieldInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public CustomFeildGetResponseDTO getCustomFieldFromBc(int bcProductId) throws PCProductFrequentException {
        PCBigCommerceClient restClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
        CustomFeildGetResponseDTO customFeildGetResponseDTO = restClient.getCustomFeilds(bcProductId, secrets.getStorehash(), secrets.getAccessToken());
        return customFeildGetResponseDTO;
    }

    public void deleteCustomField(int bcProductId, int bcCustomId, PCProductCustomFieldTransaction customData) {
        try {
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            Response response = RestClient.deleteCustomField(bcProductId, bcCustomId, secrets.getStorehash(), secrets.getAccessToken());
            String bcResponse = null;
            if (response != null & response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            if (response.status() == 204) {
                log.info("Custom field has been deleted in bigcommerce successfully : " + bcCustomId + " product id : " + bcProductId);
                PCAdisLogService.CustomFeildToAdis(PCConstants.DELETE, customData, bcProductId, bcCustomId);
                PCAdisLogService.CustomFeildToAdisLog(customData.getName(), SUCCESS, PCConstants.DELETE, null, null, customData.getProductCustomFieldsId(), customData.getDestinationProductCustomFieldsId());
            } else {
                log.info("Deleted a custom field data in bigcommerce failed : " + bcResponse);
                PCAdisLogService.CustomFeildToAdis(PCConstants.FAILED, customData, bcProductId, bcCustomId);
                PCAdisLogService.CustomFeildToAdisLog(customData.getName(), FAILED, PCConstants.DELETE, null, bcResponse, customData.getProductCustomFieldsId(), customData.getDestinationProductCustomFieldsId());
            }
        } catch (Exception e) {
            log.error("Exception -> BigcommerceRestService : deleteCustomField " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteCustomField", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public String fetchCategroyDetailsFromBC() {
        log.info("BigcommerceRestService -> fetchCategroyDetailsFromBC method started");
        try {
            CategoryResponseDTO categoryData = null;
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            int page = 1;
            int totalPage = 0;
            do {

                Response response = RestClient.getCategoryAll(secrets.getStorehash(), secrets.getAccessToken(), page);
                String bcCategoryResponse = null;
                if (response != null & response.body() != null) {
                    bcCategoryResponse = IOUtils.toString(response.body().asReader());
                }

                log.info("Category Response: " + bcCategoryResponse);
                ObjectMapper mapper = new ObjectMapper();
                if (response.status() == 200) {
                    categoryData = mapper.readValue(bcCategoryResponse, new TypeReference<CategoryResponseDTO>() {
                    });
                    totalPage = categoryData.getMeta().getPagination().getTotal_pages();
                    PCAdisLogService.CategoryOneTimeSyncToAdis(categoryData);
                } else {
                    //log
                }
                page++;
            } while (page <= totalPage);
            log.info("BigcommerceRestService -> fetchCategroyDetailsFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
    public String fetchCategroyTreeDetailsFromBC() {
        log.info("BigcommerceRestService -> fetchCategroyTreeDetailsFromBC method started");
        try {
            CategoryTreeResponseDTO categoryData = null;
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            int page = 1;
            int totalPage = 0;
            do {
          
                Response response = RestClient.getCategoryByTreeID( secrets.getStorehash(), secrets.getAccessToken(), page , 2);
                String bcCategoryResponse = IOUtils.toString(response.body().asReader());
                log.info("Category Response: " + bcCategoryResponse);
                ObjectMapper mapper = new ObjectMapper();
                if (response.status() == 200) {
                    categoryData = mapper.readValue(bcCategoryResponse, new TypeReference<CategoryTreeResponseDTO>() {
                    });
                    totalPage = categoryData.getMeta().getPagination().getTotal_pages();
                    PCAdisLogService.CategoryTreeOneTimeSyncToAdis(categoryData);
                } else {
                    //log
                }
                page++;
            } while (page <= totalPage);
            log.info("BigcommerceRestService -> fetchCategroyDetailsFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String fetchCategoryTreeByCategoryId(int categoryId) {
        log.info("BigcommerceRestService -> fetchCategoryTreeByCategoryId method started");
        try {
            CategoryTreeResponseDTO categoryData = null;
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            int page = 1;
            int totalPage = 0;
            do {
          
                Response response = RestClient.getCategoryByID( secrets.getStorehash(), secrets.getAccessToken(), page , categoryId);
                String bcCategoryResponse = IOUtils.toString(response.body().asReader());
                log.info("Category Response: " + bcCategoryResponse);
                ObjectMapper mapper = new ObjectMapper();
                if (response.status() == 200) {
                    categoryData = mapper.readValue(bcCategoryResponse, new TypeReference<CategoryTreeResponseDTO>() {
                    });
                    totalPage = categoryData.getMeta().getPagination().getTotal_pages();
                    PCAdisLogService.CategorySyncByIdToAdis(categoryData);
                } else {
                    //log
                }
                page++;
            } while (page <= totalPage);
            log.info("BigcommerceRestService -> fetchCategroyDetailsFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String fetchBrandDetailsFromBC() {
        log.info("BigcommerceRestService -> fetchBrandDetailsFromBC method started");
        try {
            BrandResponseDTO brandData = null;
            PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            int page = 1;
            int totalPage = 0;
            do {

                Response response = RestClient.getBrandAll(secrets.getStorehash(), secrets.getAccessToken(), page);
                String bcBrandResponse = null;
                if (response != null & response.body() != null) {
                    bcBrandResponse = IOUtils.toString(response.body().asReader());
                }

                log.info("Category Response: " + bcBrandResponse);
                ObjectMapper mapper = new ObjectMapper();
                if (response.status() == 200) {
                    brandData = mapper.readValue(bcBrandResponse, new TypeReference<BrandResponseDTO>() {
                    });
                    totalPage = brandData.getMeta().getPagination().getTotal_pages();
                    PCAdisLogService.brandOneTimeSyncToAdis(brandData);
                } else {
                    //log
                }
                page++;
            } while (page <= totalPage);
            log.info("BigcommerceRestService -> fetchBrandDetailsFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate Brand from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String fetchProductDetailsFromBC() {
        try {
            int bcProductId = 0;
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient BcClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());
            int page = 1;
            int totalPage = 0;
            String includeFields = "custom_fields, options, images, variants";
            do {
                log.info("running pagination for page=" + page + " totalPage=" + totalPage);
                Response response = BcClient.getProductAll(secrets.getStorehash(), secrets.getAccessToken(), includeFields, page);
                String bcresponse = null;
                if (response != null & response.body() != null) {
                    bcresponse = IOUtils.toString(response.body().asReader());
                }

                log.info("response status from Big commerce :" + bcresponse);
                if (response.status() == 200) {
                    ProductAllResponseDTO productresponse = mapper.readValue(bcresponse, new TypeReference<ProductAllResponseDTO>() {
                    });

                    totalPage = productresponse.getMeta().getPagination().getTotal_pages();

                    ArrayList<ProductDTO> bcProduct = productresponse.getData();
                    for (ProductDTO productData : bcProduct) {
                        bcProductId = productData.getId();
                        PCAdisLogService.bcProductOneTimeSync(bcProductId, productData);
                    }
                } else {
                    PCAdisLogService.insertProductLog(0, SUCCESS, PCConstants.INSERT, "Fetch order details from Big commerce.", bcresponse, bcProductId);
                }
                page++;
            } while (page <= totalPage);

        } catch (Exception Ex) {
            log.error("Exception -> BigcommerceRestService : CreateProductInBc " + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CreateProductInBc", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
        return PCConstants.SUCCESS;
    }

    public String fetchSingleProductFromBC(int productId) {
        try {
            // Create ObjectMapper for JSON processing
            ObjectMapper mapper = new ObjectMapper();

            // Build the BigCommerce client
            PCBigCommerceClient bcClient = PCRestClientBuilder.createSecureClientFormBuilder(
                    PCBigCommerceClient.class, secrets.getBcBaseUrl()
            );

            // Define the includeFields parameter
            String includeFields = "custom_fields, options, images, variants";

            // Call the Feign method to fetch the single product
            Response response = bcClient.getProductById(
                    secrets.getStorehash(),
                    secrets.getAccessToken(),
                    productId,
                    includeFields
            );

            String bcResponse = null;
            if (response != null && response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            log.info("Response status from BigCommerce: " + response.status());
            log.info("Response body: " + bcResponse);

            // Check if the response is successful
            if (response.status() == 200) {
                // Parse the response body into a ProductDTO object
                ProductResponseDTO productResponseData = mapper.readValue(bcResponse, new TypeReference<ProductResponseDTO>() {
                });

                // Process the product data
                PCAdisLogService.bcProductOneTimeSync(productId, productResponseData.getData());
                log.info("Successfully fetched product with ID: " + productId);

                return PCConstants.SUCCESS;
            } else {
                // Log the failure response
                PCAdisLogService.insertProductLog(
                        productId,
                        PCConstants.FAILED,
                        PCConstants.INSERT,
                        "Fetch single product details from BigCommerce",
                        bcResponse,
                        productId
                );

                return PCConstants.FAILED;
            }
        } catch (Exception ex) {
            log.error("Exception -> BigcommerceRestService: fetchSingleProductFromBC " + ProductUtility.getStackTrace(ex));
            emailService.sendErrorMail(
                    secrets.getClientName(),
                    "fetchSingleProductFromBC",
                    ProductUtility.getStackTrace(ex),
                    PCConstants.DEVELOPER_MAIL_SUB,
                    config.getPrefix()
            );
            return PCConstants.FAILED;
        }
    }
    


    public Integer fetchInventoryLevelOfAProductFromBC(int destinationProductID) {
        log.info("PCBigcommerceRestService.fetchInventoryLevelOfAProductFromBC started destinationProductID:" + destinationProductID);
        Integer invQty = null;

        try {
            // Create ObjectMapper for JSON processing
            ObjectMapper mapper = new ObjectMapper();

            // Build the BigCommerce client
            PCBigCommerceClient bcClient = PCRestClientBuilder.createSecureClientFormBuilder(
                    PCBigCommerceClient.class, secrets.getBcBaseUrl()
            );

            // Define the includeFields parameter
            String includeFields = "custom_fields, options, images, variants";

            // Call the Feign method to fetch the single product
            Response response = bcClient.getProductById(
                    secrets.getStorehash(),
                    secrets.getAccessToken(),
                    destinationProductID,
                    includeFields
            );

            String bcResponse = null;
            if (response != null && response.body() != null) {
                bcResponse = IOUtils.toString(response.body().asReader());
            }

            log.info("Response status from BigCommerce: " + response.status());
            log.info("Response body: " + bcResponse);

            // Check if the response is successful
            if (response.status() == 200) {
                // Parse the response body into a ProductDTO object
                ProductResponseDTO productData = mapper.readValue(bcResponse, new TypeReference<ProductResponseDTO>() {
                });


                // Process the product data
                // PCAdisLogService.bcProductOneTimeSync(productId, productData);
                log.info("Successfully fetched product with ID: " + destinationProductID);
                log.info("Inventory level of Product: " + productData.getData().getId() + ", Inventory Level: " + productData.getData().getInventory_level());
                invQty = productData.getData().getInventory_level();
            } else {
                // Log the failure response
                PCAdisLogService.insertProductLog(
                        destinationProductID,
                        PCConstants.FAILED,
                        PCConstants.INSERT,
                        "Fetch single product details from BigCommerce",
                        bcResponse,
                        destinationProductID
                );

                emailService.sendErrorMail(
                        secrets.getClientName(),
                        "fetchInventoryLevelOfAProductFromBC",
                        String.valueOf(response.status()),
                        PCConstants.DEVELOPER_MAIL_SUB,
                        config.getPrefix()
                );

            }
        } catch (Exception ex) {
            log.error("Exception -> BigcommerceRestService: fetchInventoryLevelOfAProductFromBC " + ProductUtility.getStackTrace(ex));
            emailService.sendErrorMail(
                    secrets.getClientName(),
                    "fetchInventoryLevelOfAProductFromBC",
                    ProductUtility.getStackTrace(ex),
                    PCConstants.DEVELOPER_MAIL_SUB,
                    config.getPrefix()
            );

        }

        log.info("PCBigcommerceRestService.fetchInventoryLevelOfAProductFromBC started destinationProductID:" + destinationProductID + " invQty:" + invQty);

        return invQty;
    }


    public String fetchSearchContextFilters() {
        log.info("BigcommerceRestService -> fetchSearchContextFilters method started");
        try {
        	ContextFilterResponseDTO contextFilterData = null;
			PCBigCommerceClient RestClient = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, secrets.getBcBaseUrl());

			Response response = RestClient.getAllContextFilters(secrets.getStorehash(), secrets.getAccessToken());
			String contextFiltersResponse = IOUtils.toString(response.body().asReader());
			log.info("Filter Response: " + contextFiltersResponse);
			ObjectMapper mapper = new ObjectMapper();
			if (response.status() == 200) {
				contextFilterData = mapper.readValue(contextFiltersResponse, new TypeReference<ContextFilterResponseDTO>() {
				});
				PCAdisLogService.insertFilters(contextFilterData);
			} else {
				// log
			}
            log.info("BigcommerceRestService -> fetchSearchContextFilters method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
}
