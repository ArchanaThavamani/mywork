/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.service;

import com.arizon.productcommon.config.PCConstants;

import static com.arizon.productcommon.config.PCConstants.ZERO;
import static com.arizon.productcommon.config.PCConstants.secrets;

import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.entity.*;
import com.arizon.productcommon.exception.PCProductFrequentException;
import com.arizon.productcommon.model.*;
import com.arizon.productcommon.repository.*;
import com.arizon.productcommon.restclient.PCBigCommerceClient;
import com.arizon.productcommon.restclient.PCRestClientBuilder;
import com.arizon.productcommon.util.ProductUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mohan.e
 */
@Slf4j
@Service
public class PCBigCommerceProductService {

    @Autowired
    PCRestClientBuilder PCRestClientBuilder;

    @Autowired
    PCProductCommonConfig commonconfig;

    @Autowired
    PCCategoryTransactionRepository categoryTransactionRepository;

    @Autowired
    PCProductCustomFeildMappingTransactionRepository customFeildMappingTransactionRepository;

    @Autowired
    PCProductCustomFieldTransactionRepository customFeildTransactionRepository;

    @Autowired
    PCAdisLogService PCAdisLogService;

    @Autowired
    PCProductTransactionRepository PCProductTransactionRepository;

    @Autowired
    PCProductCustomFieldTransactionRepository customFieldRepo;

    @Autowired
    PCBrandTransactionRepository pcBrandTransactionRepository;

    @Autowired
    private PCProductOptionSkuTransactionRespository optionSkuRepo;

    @Autowired
    private PCProductOptionTransactionRepository optionRepo;

    @Autowired
    private PCProductOptionValueTransactionRepository optionValueRepo;

    @Autowired
    private PCProductOptionSkuDetailsTransactionRepository skuDetailsRepo;

    @Autowired
    PCBigcommerceRestService restService;

    @Autowired
    PCProductCategoryMappingRepo categoryMappingTbl;

    @Autowired
    PCProductEmailService emailService;

    @Autowired
    PCProductCommonConfig config;

    @Autowired
    PCProductImageTransactionRepository pcProductImageTransactionRepository;

    @Autowired
    PCProductWebhookLogTransactionRepository productWebhookRepo;
    
    @Autowired
    PCCategoryWebhookLogTransactionRepository categoryWebhookRepo;
    
    
    public String productDelete(int BcProductID, String storehash, int productId) {
        log.info("BigCommerceProductService.ProductDelete method started");
        try {
            log.info("ProductDelete in Bc method - Started :" + BcProductID);
            restService.deleteProductInBc(BcProductID, productId);
            log.info("ProductDelete in Bc method - Ended :" + BcProductID);
            return PCConstants.SUCCESS;
        } catch (Exception Ex) {
            log.error("Exception :" + ProductUtility.getStackTrace(Ex));
            log.info("ProductDelete in Bc method - Ended :" + BcProductID);
            emailService.sendErrorMail(secrets.getClientName(), "productDelete", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public int productCreate(PCProductTransaction product) {
        try {
            product.setProductStatus(PCConstants.INPROGRESS);
            PCProductTransactionRepository.save(product);

            log.info("BigCommerceProductService.productCreate method started");
            log.info("we have create request for this product :" + product.getName());
            int ProductID = 0;
            ObjectMapper mapper = new ObjectMapper();
            ProductDTO ProductRequest = new ProductDTO();
            ProductRequest.setName(product.getName());
            ProductRequest.setSku(product.getSku());

            ProductRequest.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : null);
            ProductRequest.setType(product.getType());
            ProductRequest.setDescription(product.getDescription());

            if (product.getWeight() != 0) {
                ProductRequest.setWeight(product.getWeight());
            } else {
                ProductRequest.setWeight(new Double("1.0"));
            }
            ProductRequest.setHeight(product.getHeight());
            ProductRequest.setDepth(product.getDepth());
            ProductRequest.setWidth(product.getWidth());
            ProductRequest.setSale_price(product.getSalePrice() != null ? product.getSalePrice().doubleValue() : null);
            ProductRequest.setCost_price(product.getCostPrice() != null ? product.getCostPrice().doubleValue() : null);
            ProductRequest.setRetail_price(product.getRetailPrice() != null ? product.getRetailPrice().doubleValue() : null);
            ProductRequest.setInventory_level(product.getInventoryLevel());
            if (product.getInventoryTracking() != null) {
                ProductRequest.setInventory_tracking(product.getInventoryTracking());
            } else {
                ProductRequest.setInventory_tracking("product");
            }
            ProductRequest.setInventory_warning_level(product.getInventoryWarningLevel());
            ProductRequest.setFixed_cost_shipping_price(product.getFixedCostShippingPrice() != null ? product.getFixedCostShippingPrice().doubleValue() : null);
            ProductRequest.setIs_free_shipping(product.isFreeShipping());
            ProductRequest.setIs_visible(product.isVisible());
            if (product.getBrandId() != null && product.getBrandId() != 0) {
                Optional<PCBrandTransaction> fetchBrand = pcBrandTransactionRepository.findByDestinationBrandIdAndStorehash(product.getBrandId(), secrets.getStorehash());
                if (fetchBrand.isPresent()) {
                    PCBrandTransaction brand = fetchBrand.get();
                    ProductRequest.setBrand_id(brand.getDestinationBrandId());
                }
            }

            ProductRequest.setWarranty(product.getWarranty());
            ProductRequest.setMpn(product.getMpn());
            ProductRequest.setGtin(product.getGtin());
            ProductRequest.setUpc(product.getUpc());
            ProductRequest.setCondition(product.getCondition());
            ProductRequest.setAvailability(product.getAvailability());

            List<PCProductImageTransaction> pcProductImageTransactionList = pcProductImageTransactionRepository
                    .findByProductIdAndProductOptionSkuIdIsNullAndProductOptionRuleIdIsNullAndIsActiveAndIsDeleted(BigInteger.valueOf(product.getProductId()), true, false);

            ArrayList<ImageDTO> imageDTOList = new ArrayList<>();
            for (PCProductImageTransaction pcProductImageTransaction : pcProductImageTransactionList) {
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.set_thumbnail(pcProductImageTransaction.isThumbnail());
                imageDTO.setImage_url(pcProductImageTransaction.getImageUrl());
                imageDTOList.add(imageDTO);
            }

            ProductRequest.setImages(imageDTOList);

            Gson gson = new Gson();
            String jsonProductStr = gson.toJson(ProductRequest);
            log.info("Product create json request :" + ProductRequest);

            int result = restService.CreateProductInBc(ProductRequest, jsonProductStr, product.getProductId());
            return result;
        } catch (Exception Ex) {
            log.error("Exception Occured in productCreate Method :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "productCreate", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        return PCConstants.ZEROS;
    }

    public String updateProductCategoryInBc(int productId) {
        try {
            log.info("method enters into the update category in bc..");
            List<PCProductCategoryMapping> productCategories = categoryMappingTbl.findByProductIdAndIsActiveAndIsDeleted(productId, true, false);
            if (!productCategories.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Integer> categoryList = new ArrayList<>();
                int desProductId = 0;
                for (var category : productCategories) {
                    int categoryId = category.getCategoryId();
                    int pId = category.getProductId();
                    Optional<PCCategoryTransaction> categoryDetails = categoryTransactionRepository.findByCategoryId(categoryId);
                    Optional<PCProductTransaction> productDetails = PCProductTransactionRepository.findById(pId);
                    if (categoryDetails.isPresent() && productDetails.isPresent()) {
                        PCCategoryTransaction categoryData = categoryDetails.get();
                        PCProductTransaction product = productDetails.get();
                        desProductId = product.getDestinationProductId();
                        categoryList.add(categoryData.getDestinationCategoryId());
                    }
                }
                JSONObject ProductRequest = new JSONObject();
                ProductRequest.put("categories", categoryList);
//                String categoryJson = mapper.writeValueAsString(ProductRequest);
                log.info("product category json request : " + ProductRequest);
                restService.updateProductCategoryInBC(ProductRequest.toString(), desProductId);

            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception Occured in updateProductCategoryInBc Method :" + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateProductCategoryInBc", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String createCategoryDriver(String status) {
        try {
            log.info("BigCommerceProductService.createCategory method - Started");
            List<PCCategoryTransaction> categoryList = categoryTransactionRepository.findByStatusAndStorehashAndParentCategoryIdIsNull(status, secrets.getStorehash());
            for (PCCategoryTransaction pcCategoryTransaction : categoryList) {
                createCategoryById(pcCategoryTransaction, status);
            }

            categoryList = categoryTransactionRepository.findByStatusAndStorehash(status, secrets.getStorehash());
            for (PCCategoryTransaction pcCategoryTransaction : categoryList) {
                createCategoryById(pcCategoryTransaction, status);
            }

            log.info("BigCommerceProductService.createCategory method - ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception Occured in createCategory Method :" + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createCategory", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public void createCategoryById(PCCategoryTransaction pcCategoryTransaction, String status) throws JsonProcessingException {
        createCategory(pcCategoryTransaction);
        Optional<PCCategoryTransaction> pcCategoryTransactionOptional = categoryTransactionRepository.findByCategoryId(pcCategoryTransaction.getCategoryId());
        if (pcCategoryTransactionOptional.isPresent() && PCConstants.SUCCESS.equals(pcCategoryTransactionOptional.get().getStatus())) {
            List<PCCategoryTransaction> categorychildList = categoryTransactionRepository.findByStatusAndStorehashAndParentCategoryId(status, secrets.getStorehash(), pcCategoryTransaction.getCategoryId());
            for (PCCategoryTransaction pcCategoryChildTransaction : categorychildList) {
                pcCategoryChildTransaction.setDestinationParentCategoryId(pcCategoryTransaction.getDestinationCategoryId());
                createCategoryById(pcCategoryChildTransaction, status);
            }
        }
    }

    public void createCategory(PCCategoryTransaction categories) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (categories.getDestinationCategoryId() == null || categories.getDestinationCategoryId() == ZERO) {

            CategoryRequestDTO categoryRequest = new CategoryRequestDTO();
            categoryRequest.setName(categories.getName());
            categoryRequest.setDescription(categories.getDescription());
            categoryRequest.set_visible(categories.isVisible());
            categoryRequest.setSort_order(categories.getSortOrder());
            categoryRequest.setDefault_product_sort(categories.getDefaultProductSort());
            categoryRequest.setLayout_file(categories.getLayoutFile());
            categoryRequest.setSearch_keywords(categories.getSearchKeywords());
            categoryRequest.setPage_title(categories.getPageTitle());
            categoryRequest.setParent_id(categories.getDestinationParentCategoryId());
            if (categories.getParentCategoryId() == null) {
                categoryRequest.setParent_id(0);
            }

            String categoryReqJson = mapper.writeValueAsString(categoryRequest);
            log.info("Category json request : " + categoryReqJson);
            restService.createCategoryInBc(categoryReqJson, categories);
        } else {
            int desCategoryId = categories.getDestinationCategoryId();
            int bcParentCategoryId = categories.getDestinationParentCategoryId();
            if (categories.isActive()) {
                JSONObject categoryRequest = new JSONObject();
                categoryRequest.put("name", categories.getName());
                if (categories.getDescription() == null) {
                    categoryRequest.put("description", categories.getDescription());
                }

                log.info("Category update json request : " + categoryRequest);
                restService.updateCategoryInBc(categoryRequest.toString(), categories);
            } else {
                log.info("Category delete request for - id : " + desCategoryId + " name : " + categories.getName());
                restService.deleteCategoryInBc(categories);
            }
        }
    }

    public String customFieldValidate(int productId, String status) {
        try {
            log.info("BigCommerceProductService.customFeildsCreate method - Started");
            ObjectMapper mapper = new ObjectMapper();
            List<PCProductCustomFieldTransaction> customFieldData = customFieldRepo.findByProductIdAndStatus(BigInteger.valueOf(productId), status);
            Optional<PCProductTransaction> product = PCProductTransactionRepository.findById(productId);
            Integer bcProductId = product.get().getDestinationProductId();
            if (!customFieldData.isEmpty()) {
                for (var field : customFieldData) {
                    if (field.getDestinationProductCustomFieldsId() == null || field.getDestinationProductCustomFieldsId() == ZERO) {
                        JSONObject obj = new JSONObject();
                        obj.put("name", field.getName());
                        obj.put("value", field.getValue());
                        log.info("Create request json for custom field : " + obj);
                        restService.CreateCustomFieldInBc(obj.toString(), bcProductId, field);
                    } else {
                        Integer bcCustomId = field.getDestinationProductCustomFieldsId();
                        if (field.isActive()) {
                            CustomFieldDTO updateCustom = new CustomFieldDTO();
                            updateCustom.setId(bcCustomId);
                            updateCustom.setName(field.getName());
                            updateCustom.setValue(field.getValue());
                            String CustomUpdateReqJson = mapper.writeValueAsString(updateCustom);
                            log.info("Update request json for custom field : " + CustomUpdateReqJson);
                            restService.updateCustomFieldInBc(CustomUpdateReqJson, bcProductId, bcCustomId, field);
                        } else {
                            log.info("Delete request data for custom field : " + bcCustomId + "Bc Product id" + bcProductId);
                            restService.deleteCustomField(bcProductId, bcCustomId, field);
                        }
                    }

                }
            } else {
                log.info("There are no custom field data for product : " + bcProductId);
            }
            log.info("BigCommerceProductService.customFeildsCreate method - Ended");
            return PCConstants.SUCCESS;
        } catch (Exception Ex) {
            log.info("BigCommerceProductService.customFeildsCreate method - Ended");
            log.error("Exception Occured in customFeildsCreate Method :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "customFeildsCreate", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String createBrand(String status) {
        log.info("BigCommerceProductService.createBrand method started");
        Integer BcBrandId;
        try {
            List<PCBrandTransaction> brands = pcBrandTransactionRepository.findByStatusAndStorehash(status, secrets.getStorehash());

            if (!brands.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                for (var brand : brands) {
                    BcBrandId = brand.getDestinationBrandId();
                    if (brand.isActive()) {

                        if (BcBrandId == null || BcBrandId == ZERO) {
                            JSONObject brandRequest = new JSONObject();
                            brandRequest.put("name", brand.getName());

                            log.info(" create brand json request : " + brandRequest.toString());
                            BcBrandId = restService.createBrandInBc(brandRequest.toString(), brand, brand.getBrandId());
                        }
                    } else {
                        log.info("Received the brand request as active - false, It will delete in bc.");
                        restService.deleteBrandInBc(BcBrandId, brand, brand.getBrandId());
                    }
                }
            } else {
                log.info("No pending data from Brand ADIS");
            }
            log.info("BigCommerceProductService.createBrand method ended");
            return PCConstants.SUCCESS;
        } catch (Exception Ex) {
            log.error("Exception occured in createBrand :" + ProductUtility.getStackTrace(Ex));
            log.info("BigCommerceProductService.createBrand method ended");
            emailService.sendErrorMail(secrets.getClientName(), "ValidateBrand", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }

    }

    public String updateProduct(int BCproductID, int productId, String mode) {
        try {

            log.info("BigCommerceProductService.updateProduct method - Started");

            Optional<PCProductTransaction> optionalProduct = PCProductTransactionRepository.findByProductIdAndIsActive(productId, true);
            if (optionalProduct.isPresent()) {
                PCProductTransaction product = optionalProduct.get();

                ProductDTO ProductRequest = new ProductDTO();
                ProductRequest.setId(BCproductID);

                if ("Visible".equalsIgnoreCase(mode)) {
                    ProductRequest.setIs_visible(product.isVisible());
                } else if ("Price".equalsIgnoreCase(mode)) {
                    ProductRequest.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : null);
                    ProductRequest.setRetail_price(product.getRetailPrice() != null ? product.getRetailPrice().doubleValue() : null);
                } else {
                    ProductRequest.setName(product.getName());
                    ProductRequest.setSku(product.getSku());
                    ProductRequest.setType("physical");
                    ProductRequest.setDescription(product.getDescription());

                    if (product.getWeight() != 0) {
                        ProductRequest.setWeight(product.getWeight());
                    } else {
                        ProductRequest.setWeight(new Double("1.0"));
                    }
                    ProductRequest.setHeight(product.getHeight());
                    ProductRequest.setDepth(product.getDepth());
                    ProductRequest.setWidth(product.getWidth());
                    ProductRequest.setPrice(product.getPrice() != null ? product.getPrice().doubleValue() : null);
                    ProductRequest.setSale_price(product.getSalePrice() != null ? product.getSalePrice().doubleValue() : null);
                    ProductRequest.setCost_price(product.getCostPrice() != null ? product.getCostPrice().doubleValue() : null);
                    ProductRequest.setRetail_price(product.getRetailPrice() != null ? product.getRetailPrice().doubleValue() : null);

                    if (product.getInventoryTracking() != null) {
                        ProductRequest.setInventory_tracking(product.getInventoryTracking());
                    } else {
                        ProductRequest.setInventory_tracking("product");
                    }

                    ProductRequest.setInventory_level(product.getInventoryLevel());
                    ProductRequest.setInventory_warning_level(product.getInventoryWarningLevel());
                    ProductRequest.setFixed_cost_shipping_price(product.getFixedCostShippingPrice() != null ? product.getFixedCostShippingPrice().doubleValue() : null);
                    ProductRequest.setIs_free_shipping(product.isFreeShipping());
                    ProductRequest.setIs_visible(product.isVisible());

                    if (product.getBrandId() != null && product.getBrandId() != 0) {
                        Optional<PCBrandTransaction> fetchBrand = pcBrandTransactionRepository.findByDestinationBrandIdAndStorehash(product.getBrandId(), secrets.getStorehash());
                        if (fetchBrand.isPresent()) {
                            PCBrandTransaction brand = fetchBrand.get();
                            ProductRequest.setBrand_id(brand.getDestinationBrandId());
                        }
                    }

                    ProductRequest.setWarranty(product.getWarranty());
                    ProductRequest.setMpn(product.getMpn());
                    ProductRequest.setGtin(product.getGtin());
                    ProductRequest.setUpc(product.getUpc());
                    ProductRequest.setCondition(product.getCondition());
                    ProductRequest.setAvailability(product.getAvailability());
                }

                Gson gson = new Gson();
                String jsonProductStr = gson.toJson(ProductRequest);
                log.info("Product update json request : " + jsonProductStr);

                restService.updateProductInBc(jsonProductStr, BCproductID, product.getProductId());
            } else {
                log.info("Update product -> Bc Product Id doesn't exists in ADIS table... " + BCproductID);
            }
            log.info("BigCommerceProductService.updateProduct method - Ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("BigCommerceProductService.updateProduct method ended   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateProduct", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String fetchOptionSkuDetailsADIS(PCProductTransaction product, String status) {
        try {
            List<PCProductOptionSkuTransaction> optionSkuList = optionSkuRepo.findByProductIDAndStatus(product.getProductId(), status);
            for (PCProductOptionSkuTransaction optionSku : optionSkuList) {
                if (optionSku.getDestinationSkuID() == null || optionSku.getDestinationSkuID() == 0) {
                    createOptionSkuBC(optionSku, product.getDestinationProductId());
                } else {
                    if (optionSku.isActive()) {
                        updateOptionSkuBC(optionSku, product.getDestinationProductId());
                    } else {
                        deleteOptionSkuBC(optionSku, product.getDestinationProductId());
                    }
                }
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on BigCommerceProductService.fetchOptionSkuDetailsADIS   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "fetchOptionSkuDetailsADIS", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }

    }

    public String createOptionSkuBC(PCProductOptionSkuTransaction optionSku, int destinationProductID) {
        log.info("Entered into BigCommerceProductService.createOptionSku method ");
        try {

            ArrayList<OptionValue> optionValueReqList = fetchOptionValueForOptionSkuCreation(optionSku, destinationProductID);
            if (optionValueReqList.isEmpty()) {
                log.info("Empty option values for a variant  :  " + optionSku.getProductOptionSkuID());
                return PCConstants.FAILED;
            }
            ProductSkuDTO productSkuReq = new ProductSkuDTO();
            productSkuReq.setProduct_id(destinationProductID);
            productSkuReq.setOption_values(optionValueReqList);
            productSkuReq.setSku(optionSku.getSku());
            productSkuReq.setFixed_cost_shipping_price(optionSku.getFixedCostShippingPrice().intValue());
            productSkuReq.setInventory_level(optionSku.getInventoryLevel().intValue());
            productSkuReq.setInventory_warning_level(optionSku.getInventoryWarningLevel());
            productSkuReq.setPrice(optionSku.getPrice().intValue());
            productSkuReq.setCost_price(optionSku.getCostPrice().intValue());
            productSkuReq.setRetail_price(optionSku.getRetailPrice().intValue());
            productSkuReq.setSale_price(optionSku.getSalePrice().intValue());
            productSkuReq.setHeight((int) optionSku.getHeight());
            productSkuReq.setDepth((int) optionSku.getDepth());
            productSkuReq.setWeight(BigDecimal.valueOf(optionSku.getWeight()));
            productSkuReq.setWidth((int) optionSku.getWidth());
            productSkuReq.set_free_shipping(optionSku.isFreeShipping());
            productSkuReq.setPurchasing_disabled(optionSku.isPurchasingDisabled());
            if (optionSku.getPurchasingDisabledMessage() != null) {
                productSkuReq.setPurchasing_disabled_message(optionSku.getPurchasingDisabledMessage());
            }
            if (optionSku.getBinPickingNumber() != null) {
                productSkuReq.setBin_picking_number(optionSku.getBinPickingNumber());
            }
            if (optionSku.getUpc() != null) {
                productSkuReq.setUpc(optionSku.getUpc());
            }

            Gson gson = new Gson();
            String productSkuStr = gson.toJson(productSkuReq);
            log.info("Option Sku creation request  :  " + productSkuStr);

            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.createProductVariant(secrets.getStorehash(), secrets.getAccessToken(), productSkuStr, destinationProductID);
            String createProductVariantResponse = IOUtils.toString(feignResponse.body().asReader());

            if (feignResponse.status() == 200) {
                int destinationOptionSkuID = new JSONObject(createProductVariantResponse).getJSONObject("data").getInt("id");
                optionSku.setDestinationSkuID(destinationOptionSkuID);
                optionSku.setStatus(PCConstants.SUCCESS);
                optionSkuRepo.save(optionSku);
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), destinationOptionSkuID, PCConstants.SUCCESS, productSkuStr, createProductVariantResponse, PCConstants.CREATE);
            } else {
                optionSku.setStatus(PCConstants.FAILED);
                optionSkuRepo.save(optionSku);
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), 0, PCConstants.FAILED, productSkuStr, createProductVariantResponse, PCConstants.CREATE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | JSONException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.createOptionSku   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createOptionSku", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String updateOptionSkuBC(PCProductOptionSkuTransaction optionSku, int destinationProductID) {
        log.info("Entered into BigCommerceProductService.updateOptionSkuBC method ");
        try {
            ArrayList<OptionValue> optionValueReqList = fetchOptionValueForOptionSkuCreation(optionSku, destinationProductID);
            if (optionValueReqList.isEmpty()) {
                log.info("Empty option values for a variant  :  " + optionSku.getProductOptionSkuID());
                return PCConstants.FAILED;
            }

            ProductSkuDTO productSkuReq = new ProductSkuDTO();
            productSkuReq.setOption_values(optionValueReqList);
            productSkuReq.setSku(optionSku.getSku());
            if (optionSku.getBinPickingNumber() != null) {
                productSkuReq.setBin_picking_number(optionSku.getBinPickingNumber());
            }
            productSkuReq.setCost_price(optionSku.getCostPrice().intValue());
            productSkuReq.setDepth((int) optionSku.getDepth());
            productSkuReq.setFixed_cost_shipping_price(optionSku.getFixedCostShippingPrice().intValue());
            productSkuReq.setHeight((int) optionSku.getHeight());
            productSkuReq.setInventory_level(optionSku.getInventoryLevel().intValue());
            productSkuReq.setInventory_warning_level(optionSku.getInventoryWarningLevel());
            productSkuReq.setPrice(optionSku.getPrice().intValue());
            productSkuReq.setProduct_id(destinationProductID);
            productSkuReq.setRetail_price(optionSku.getRetailPrice().intValue());
            productSkuReq.setSale_price(optionSku.getSalePrice().intValue());
            if (optionSku.getUpc() != null) {
                productSkuReq.setUpc(optionSku.getUpc());
            }
            productSkuReq.setWeight(BigDecimal.valueOf(optionSku.getWeight()));
            productSkuReq.setWidth((int) optionSku.getWidth());
            productSkuReq.set_free_shipping(optionSku.isFreeShipping());
            productSkuReq.setPurchasing_disabled(optionSku.isPurchasingDisabled());
            if (optionSku.getPurchasingDisabledMessage() != null) {
                productSkuReq.setPurchasing_disabled_message(optionSku.getPurchasingDisabledMessage());
            }

            Gson gson = new Gson();
            String productSkuStr = gson.toJson(productSkuReq);
            log.info("Option Sku update request  :  " + productSkuStr);

            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.updateProductVariant(secrets.getStorehash(), secrets.getAccessToken(), productSkuStr, destinationProductID, optionSku.getDestinationSkuID());
            String updateProductVariantResponse = IOUtils.toString(feignResponse.body().asReader());

            if (feignResponse.status() == 200) {
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), optionSku.getDestinationSkuID(), PCConstants.SUCCESS, productSkuStr, updateProductVariantResponse, PCConstants.UPDATE);
                optionSku.setStatus(PCConstants.SUCCESS);
                optionSkuRepo.save(optionSku);
            } else {
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), optionSku.getDestinationSkuID(), PCConstants.FAILED, productSkuStr, updateProductVariantResponse, PCConstants.UPDATE);
                optionSku.setStatus(PCConstants.FAILED);
                optionSkuRepo.save(optionSku);
            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on BigCommerceProductService.updateOptionSkuBC   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateOptionSkuBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String deleteOptionSkuBC(PCProductOptionSkuTransaction optionSku, int destinationProductID) {
        log.info("Entered into BigCommerceProductService.deleteOptionSku method  ");
        try {
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.deleteProductVariant(secrets.getStorehash(), secrets.getAccessToken(), destinationProductID, optionSku.getDestinationSkuID().intValue());
            if (feignResponse.status() == 204) {
                log.info("The variant successfully deleted in BC  ");
                optionSku.setStatus(PCConstants.SUCCESS);
                optionSkuRepo.save(optionSku);
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), optionSku.getDestinationSkuID(), PCConstants.SUCCESS, PCConstants.DELETE, "Sucessfully deleted", PCConstants.DELETE);

            } else {
                String response = IOUtils.toString(feignResponse.body().asReader());
                optionSku.setStatus(PCConstants.FAILED);
                optionSkuRepo.save(optionSku);
                log.info("Failed to delete the product variant in BC  : " + optionSku.getDestinationSkuID());
                PCAdisLogService.storeProductOptionSkuLog(optionSku.getProductOptionSkuID(), optionSku.getDestinationSkuID(), PCConstants.FAILED, PCConstants.DELETE, response, PCConstants.DELETE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.deleteOptionSku   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteOptionSku", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    private ArrayList<OptionValue> fetchOptionValueForOptionSkuCreation(PCProductOptionSkuTransaction optionSku, int destinationProductID) {
        log.info("Entered into BigCommerceProductService.fetchOptionValueForOptionSkuCreation method  ");
        try {

            int destinationOptionID = 0;
            int destinationOptionValueID = 0;
            ArrayList<OptionValue> optionValueReqList = new ArrayList<>();
            List<PCProductOptionSkuDetailsTransaction> skuDetailsList = skuDetailsRepo.findByProductOptionSkuID(optionSku.getProductOptionSkuID());
            log.info("skuDetailsList  :  " + skuDetailsList);
            for (PCProductOptionSkuDetailsTransaction skuDetail : skuDetailsList) {
                OptionValue optionValueReq = new OptionValue();
                Optional<PCProductOptionTransaction> optionalProductOption = optionRepo.findById(skuDetail.getOptionID().intValue());
                Optional<PCProductOptionValueTransaction> optionalProductOptionValue = optionValueRepo.findById(skuDetail.getOptionValueID().intValue());

                if (optionalProductOption.isPresent() && optionalProductOptionValue.isPresent()) {

                    PCProductOptionTransaction productOption = optionalProductOption.get();
                    PCProductOptionValueTransaction productOptionValue = optionalProductOptionValue.get();
                    if (productOption.getDestinationOptionID() != null) {
                        destinationOptionID = productOption.getDestinationOptionID().intValue();
                    }
                    if (productOptionValue.getDestinationOptionValueID() != null) {
                        destinationOptionValueID = productOptionValue.getDestinationOptionValueID().intValue();
                    }
                    if (destinationOptionID != 0 && destinationOptionValueID != 0) {
                        log.info("option and values exist ");
                        optionValueReq.setId(destinationOptionValueID);
                        optionValueReq.setOption_id(destinationOptionID);
                        optionValueReq.setLabel(productOptionValue.getLabel());
                        optionValueReq.setOption_display_name(productOption.getDisplayName());
                        optionValueReqList.add(optionValueReq);

                        skuDetail.setDestinationProductOptionSkuID(destinationOptionID);
                        skuDetailsRepo.save(skuDetail);
                    } else if (productOption.getDestinationOptionID() == null || productOption.getDestinationOptionID().intValue() == 0) {
                        log.info("option not availabe");
                        destinationOptionID = createProductOptionBC(productOption, destinationProductID, productOptionValue);
                        if (destinationOptionID != 0) {

                            optionalProductOptionValue = optionValueRepo.findById(skuDetail.getOptionValueID().intValue());
                            productOptionValue = optionalProductOptionValue.get();
                            destinationOptionValueID = productOptionValue.getDestinationOptionValueID().intValue();
                            optionValueReq.setId(destinationOptionValueID);
                            optionValueReq.setOption_id(destinationOptionID);
                            optionValueReq.setLabel(productOptionValue.getLabel());
                            optionValueReq.setOption_display_name(productOption.getDisplayName());
                            optionValueReqList.add(optionValueReq);
                            skuDetail.setDestinationProductOptionSkuID(destinationOptionID);
                            skuDetailsRepo.save(skuDetail);
                        } else {
                            return new ArrayList<>();
                        }
                    } else if (productOptionValue.getDestinationOptionValueID() == null || productOptionValue.getDestinationOptionValueID().intValue() == 0) {
                        log.info("option value not availabe");
                        destinationOptionValueID = createOptionValueBC(productOptionValue, destinationProductID, destinationOptionID);
                        if (destinationOptionValueID != 0) {
                            optionValueReq.setId(destinationOptionValueID);
                            optionValueReq.setOption_id(destinationOptionID);
                            optionValueReq.setLabel(productOptionValue.getLabel());
                            optionValueReq.setOption_display_name(productOption.getDisplayName());
                            optionValueReqList.add(optionValueReq);

//                            skuDetail.setDestinationProductOptionSkuID(destinationOptionID);
//                            skuDetailsRepo.save(skuDetail);
                        } else {
                            return new ArrayList<>();
                        }
                    }

                }
            }
            return optionValueReqList;
        } catch (Exception e) {
            log.error("Exception on BigCommerceProductService.fetchOptionValueForOptionSkuCreation  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "fetchOptionValueForOptionSkuCreation", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return new ArrayList<>();
        }
    }

    public int createProductOptionBC(PCProductOptionTransaction productOption, int destinationProductID, PCProductOptionValueTransaction optionValue) {
        log.info("Entered into BigCommerceProductService.createProductOption method ");
        try {
            int destinationOptionID = 0;
            OptionData optionDataReq = new OptionData();
            optionDataReq.setProduct_id(destinationProductID);
            optionDataReq.setDisplay_name(productOption.getDisplayName());
//            optionData.setName(productOption.get);
            optionDataReq.setType(productOption.getType());
            ArrayList<OptionValue> optionValueListReq = new ArrayList<>();

            OptionValue optionValueReq = new OptionValue();
            optionValueReq.setLabel(optionValue.getLabel());
            if (optionValue.getSortOrder() != null) {
                optionValueReq.setSort_order(optionValue.getSortOrder());
            } else {
                optionValueReq.setSort_order(0);
            }
            optionValueReq.set_default(optionValue.isDefault());

            if (optionValue.getDestinationOptionValueID() == null || optionValue.getDestinationOptionValueID().intValue() == 0) {
                optionValueReq.setId(0);
            } else {
                optionValueReq.setId(optionValue.getDestinationOptionValueID().intValue());
            }
            optionValueListReq.add(optionValueReq);

            optionDataReq.setOption_values(optionValueListReq);

            Gson gson = new Gson();
            String optionDataStr = gson.toJson(optionDataReq);

            log.info("productOption request  :  " + optionDataStr);
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.createProductOption(optionDataStr, destinationProductID, secrets.getStorehash(), secrets.getAccessToken());
            String createOptionResponse = IOUtils.toString(feignResponse.body().asReader());
            if (feignResponse.status() == 200) {

                OptionResponseRootDTO responseRoot = mapper.readValue(createOptionResponse, new TypeReference<OptionResponseRootDTO>() {
                });
                log.info("create product Option BC  response  :  " + responseRoot);
                updateOptionValueIdADIS(productOption, responseRoot.getData().getOption_values());
                destinationOptionID = responseRoot.getData().getId();
                log.info("created destinationOptionID  :  " + destinationOptionID);
                productOption.setDestinationOptionID(BigInteger.valueOf(destinationOptionID));
                productOption = optionRepo.save(productOption);
                PCAdisLogService.storeProductOptionLog(productOption.getProductOptionsID(), productOption.getDestinationOptionID().intValue(), PCConstants.SUCCESS, optionDataReq.toString(), createOptionResponse, PCConstants.CREATE);
            } else {
                PCAdisLogService.storeProductOptionLog(productOption.getProductOptionsID(), productOption.getDestinationOptionID().intValue(), PCConstants.FAILED, optionDataReq.toString(), createOptionResponse, PCConstants.CREATE);
            }
            return destinationOptionID;

        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.createProductOption   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createProductOptionBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return 0;
        }
    }

    public String updateProductOptionBC(PCProductOptionTransaction productOption, int destinationProductID) {
        log.info("Entered into BigCommerceProductService.updateProductOptionBC method ");
        try {
            OptionData optionDataReq = new OptionData();
            optionDataReq.setProduct_id(destinationProductID);
            optionDataReq.setDisplay_name(productOption.getDisplayName());
//            optionData.setName(productOption.get);
            optionDataReq.setType(productOption.getType());

            ArrayList<OptionValue> optionValueListReq = new ArrayList<>();
            List<PCProductOptionValueTransaction> optionValueList = optionValueRepo.findByproductOptionsID(productOption.getProductOptionsID());
            for (PCProductOptionValueTransaction optionValue : optionValueList) {
                OptionValue optionValueReq = new OptionValue();
                optionValueReq.setLabel(optionValue.getLabel());
                optionValueReq.setSort_order(optionValue.getSortOrder());
                optionValueReq.set_default(optionValue.isDefault());
//                optionValueReq.setValue_data(optionValue.getValueData());           // need to verify
                if (optionValue.getDestinationOptionValueID().intValue() == 0) {
                    optionValueReq.setId(0);
                } else {
                    optionValueReq.setId(optionValue.getDestinationOptionValueID().intValue());
                }
                optionValueListReq.add(optionValueReq);
            }

            Gson gson = new Gson();
            String optionDataStr = gson.toJson(optionDataReq);
            log.info(" update optionData string  :  " + optionDataStr);
            ObjectMapper mapper = new ObjectMapper();
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.updateProductOption(secrets.getStorehash(), secrets.getAccessToken(), optionDataStr, destinationProductID, productOption.getDestinationOptionID().intValue());
            String updateOptionResponse = IOUtils.toString(feignResponse.body().asReader());
            log.info("updateOptionResponse  :  " + updateOptionResponse);
            if (feignResponse.status() == 200) {
                OptionResponseRootDTO responseRoot = mapper.readValue(updateOptionResponse, new TypeReference<OptionResponseRootDTO>() {
                });
                updateOptionValueIdADIS(productOption, responseRoot.getData().getOption_values());
                PCAdisLogService.storeProductOptionLog(productOption.getProductOptionsID(), productOption.getDestinationOptionID().intValue(), PCConstants.SUCCESS, optionDataStr, updateOptionResponse, PCConstants.UPDATE);
            } else {
                PCAdisLogService.storeProductOptionLog(productOption.getProductOptionsID(), productOption.getDestinationOptionID().intValue(), PCConstants.FAILED, optionDataStr, updateOptionResponse, PCConstants.UPDATE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.updateProductOptionBC  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateProductOptionBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String deleteProductOptionBC(int destinationProductID, int destinationOptionID, int productOptionID) {
        log.info("Entered into the method BigCommerceProductService.deleteProductOptionBC  ");
        try {
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.deleteProductOption(secrets.getStorehash(), secrets.getAccessToken(), destinationProductID, destinationOptionID);
            if (feignResponse.status() == 204) {
                log.info("Given product option is successfully deleted in BC  : " + destinationOptionID);
                PCAdisLogService.storeProductOptionLog(productOptionID, destinationOptionID, PCConstants.SUCCESS, PCConstants.DELETE, "Successfully Deleted", PCConstants.DELETE);
            } else {
                String response = IOUtils.toString(feignResponse.body().asReader());
                log.info("Given product option got failed to deleted in BC  : " + destinationOptionID);
                PCAdisLogService.storeProductOptionLog(productOptionID, destinationOptionID, PCConstants.FAILED, PCConstants.DELETE, response, PCConstants.DELETE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.deleteProductOptionBC  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteProductOptionBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public int createOptionValueBC(PCProductOptionValueTransaction optionValue, int destinationProductID, int destinationOptionID) {
        try {
            int destinationOptionValueID = 0;
            OptionValue optionValueReq = new OptionValue();

            optionValueReq.set_default(optionValue.isDefault());
            optionValueReq.setLabel(optionValue.getLabel());
            if (optionValue.getSortOrder() != null) {
                optionValueReq.setSort_order(optionValue.getSortOrder());
            } else {
                optionValueReq.setSort_order(PCConstants.ZEROS);
            }
//            optionValueReq.setValue_data(null);         // need to verify
//            ObjectMapper mapper = new ObjectMapper();

            Gson gson = new Gson();
            String optionValueStr = gson.toJson(optionValueReq);
            log.info("optionValue Request  :  " + optionValueStr);
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.createProductOptionValue(secrets.getStorehash(), secrets.getAccessToken(), optionValueStr, destinationProductID, destinationOptionID);
            String createOptionValueResponse = IOUtils.toString(feignResponse.body().asReader());
            log.info("createOptionValueResponse  :  " + createOptionValueResponse);
            if (feignResponse.status() == 200) {
                destinationOptionValueID = new JSONObject(createOptionValueResponse).getJSONObject("data").getInt("id");
                optionValue.setDestinationOptionValueID(BigInteger.valueOf(destinationOptionValueID));
                optionValueRepo.save(optionValue);
                PCAdisLogService.storeProductOptionValueLog(optionValue.getProductOptionValueID(), destinationOptionValueID, PCConstants.SUCCESS, optionValueStr, createOptionValueResponse, PCConstants.CREATE);
            } else {
//                log and email part.
                PCAdisLogService.storeProductOptionValueLog(optionValue.getProductOptionValueID(), destinationOptionValueID, PCConstants.FAILED, optionValueStr, createOptionValueResponse, PCConstants.CREATE);
            }
            return destinationOptionValueID;
        } catch (IOException | JSONException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.createOptionValueBC  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "createOptionValueBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return 0;
        }
    }

    public String updateOptionValueIdADIS(PCProductOptionTransaction productOption, List<OptionValue> optionValueResponseList) {
        log.info("Entered into BigCommerceProductService.updateOptionValueIdADIS method  ");
        try {
            for (OptionValue optionValueResponse : optionValueResponseList) {
                Optional<PCProductOptionValueTransaction> optionalOptionValue = optionValueRepo.findByProductOptionsIDAndLabel(productOption.getProductOptionsID(), optionValueResponse.getLabel());
                if (optionalOptionValue.isPresent()) {
                    PCProductOptionValueTransaction optionValue = optionalOptionValue.get();
                    optionValue.setDestinationOptionValueID(BigInteger.valueOf(optionValueResponse.getId()));
                    optionValueRepo.save(optionValue);
                } else {
                    log.info("Given option value is not found : " + optionValueResponse.getLabel());
                }

            }
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on BigCommerceProductService.updateOptionValueIdADIS   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateOptionValueIdADIS", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String updateOptionValueBC(PCProductOptionValueTransaction optionValue, int destinationProductID, int destinationOptionID) {
        log.info("Entered into BigCommerceProductService.updateOptionValueBC method  ");
        try {
            OptionValue optionValueReq = new OptionValue();

            optionValueReq.set_default(optionValue.isDefault());
            optionValueReq.setLabel(optionValue.getLabel());
            optionValueReq.setSort_order(optionValue.getSortOrder());
            optionValueReq.setValue_data(null);         // need to verify

            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.updateProductOptionValue(secrets.getStorehash(), secrets.getAccessToken(), optionValueReq.toString(), destinationProductID, destinationOptionID, optionValue.getDestinationOptionValueID().intValue());
            String updateProductOptionValueResponse = IOUtils.toString(feignResponse.body().asReader());
            if (feignResponse.status() == 200) {
                PCAdisLogService.storeProductOptionValueLog(optionValue.getProductOptionValueID(), optionValue.getDestinationOptionValueID().intValue(), PCConstants.SUCCESS, optionValueReq.toString(), updateProductOptionValueResponse, PCConstants.UPDATE);
            } else {
                PCAdisLogService.storeProductOptionValueLog(optionValue.getProductOptionValueID(), optionValue.getDestinationOptionValueID().intValue(), PCConstants.FAILED, optionValueReq.toString(), updateProductOptionValueResponse, PCConstants.UPDATE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.updateOptionValueBC   " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "updateOptionValueBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String deleteProductOptionValueBC(int destinationProductID, int destinationOptionID, int destinationOptionValueID, int productOptionValueID) {
        log.info("Entered into BigCommerceProductService.deleteProductOptionValueBC method  ");
        try {
            PCBigCommerceClient bcClientBuilder = PCRestClientBuilder.createSecureClientFormBuilder(PCBigCommerceClient.class, PCConstants.secrets.getBcBaseUrl());
            feign.Response feignResponse = bcClientBuilder.deleteProductOptionValue(secrets.getStorehash(), secrets.getAccessToken(), destinationProductID, destinationOptionID, destinationOptionValueID);
            if (feignResponse.status() == 204) {
                PCAdisLogService.storeProductOptionValueLog(productOptionValueID, destinationOptionValueID, PCConstants.SUCCESS, PCConstants.DELETE, "Successfully deleted", PCConstants.DELETE);
            } else {
                String response = IOUtils.toString(feignResponse.body().asReader());
                PCAdisLogService.storeProductOptionValueLog(productOptionValueID, destinationOptionValueID, PCConstants.FAILED, PCConstants.DELETE, response, PCConstants.DELETE);
            }
            return PCConstants.SUCCESS;
        } catch (IOException | PCProductFrequentException e) {
            log.error("Exception on BigCommerceProductService.deleteProductOptionValueBC  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "deleteProductOptionValueBC", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String fetchCategroyFromBC() {
        try {
            log.info("BigCommerceProductService -> fetchCategroyFromBC method started");
            restService.fetchCategroyDetailsFromBC();
            log.info("BigCommerceProductService -> fetchCategroyFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
    public String fetchCategoryTreeFromBC() {
        try {
            log.info("BigCommerceProductService -> fetchCategroyFromBC method started");
            restService.fetchCategroyTreeDetailsFromBC();
            log.info("BigCommerceProductService -> fetchCategroyFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate category from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String fetchBrandFromBC() {
        try {
            log.info("BigCommerceProductService -> fetchBrandFromBC method started");
            restService.fetchBrandDetailsFromBC();
            log.info("BigCommerceProductService -> fetchBrandFromBC method ended");
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate Brand from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public String fetchProductFromBC() {
        try {
            restService.fetchProductDetailsFromBC();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate product from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    

    public String fetchProductByIdFromBC(int destinationProductID) {
        try {
            restService.fetchSingleProductFromBC(destinationProductID);
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrate product from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }

    public Integer fetchInventoryLevelOfAProduct(Integer destinationProductID){
        try {
            Integer inventory_level = restService.fetchInventoryLevelOfAProductFromBC(destinationProductID);
            return inventory_level;
        } catch (Exception e) {
            log.error("Exception occured when integrate product from BC to ADIS" + ProductUtility.getStackTrace(e));
            return -1;
        }
    }

	public void fetchWebhookPendingProducts(String status) {
		try {
			log.info("fetchWebhookPendingProducts method started with status :" + status);
			List<PCProductWebhookLogTransaction> webhookPendingProducts = productWebhookRepo
					.findByStorehashAndStatus(secrets.getStorehash(), status);
			if (webhookPendingProducts.isEmpty()) {
				log.info("There are no products with " + status);
				return;
			}
			for (PCProductWebhookLogTransaction productWebhook : webhookPendingProducts) {
				if (productWebhook.getScope() != null && productWebhook.getScope().contains("store/product/")) {
					String productBCFetchstatus = restService.fetchSingleProductFromBC(productWebhook.getDestinationProductId());
					productWebhook.setStatus(productBCFetchstatus);
					productWebhookRepo.save(productWebhook);
				}
			}
		} catch (Exception e) {
			log.error("Exception occured in fetchWebhookPendingProducts" + ProductUtility.getStackTrace(e));
		}
	}

	public void fetchWebhookPendingCategories(String status) {
		try {
			log.info("fetchWebhookPendingCategories method started with status :" + status);
			List<PCCategoryWebhookLogTransaction> webhookPendingCategories = categoryWebhookRepo.findByStorehashAndStatus(secrets.getStorehash(), status);
			if (webhookPendingCategories.isEmpty()) {
				log.info("There are no categories with " + status);
			}
			for (PCCategoryWebhookLogTransaction categoryWebhook : webhookPendingCategories) {
				if (categoryWebhook.getScope().contains("store/category/")) {
					String categoryFetchBCFetchstatus = restService.fetchCategoryTreeByCategoryId(categoryWebhook.getDestinationCategoryId());
					categoryWebhook.setStatus(categoryFetchBCFetchstatus);
					categoryWebhookRepo.save(categoryWebhook);
				}
			}
		} catch (Exception e) {
			log.error("Exception occured in fetchWebhookPendingCategories" + ProductUtility.getStackTrace(e));
		}
	}
    
    
    
    public boolean storeQueueResponseInAdis(ProductWebhookDTO queueResponse, String status) {
        try {
            log.info("BCOrderService.storeQueueResponseInAdis method started");
            PCProductWebhookLogTransaction productWebhook = new PCProductWebhookLogTransaction();
            Optional<PCProductWebhookLogTransaction> webhookResponseFromADIS = productWebhookRepo.findByDestinationProductIdAndStorehash(queueResponse.getData().getId(), secrets.getStorehash());
            productWebhook.setDestinationProductId(queueResponse.getData().getId());
            productWebhook.setScope(queueResponse.getScope());
            if(productWebhook.getScope().contains(PCConstants.PRODUCTDELETESCOPE)) {
            	productWebhook.setStatus(PCConstants.DELETE);
            } else {
            	productWebhook.setStatus(status);            
            }
            if (webhookResponseFromADIS.isPresent()) {
                log.info("WebHook data contains Product Update data");
                productWebhook.setProductWebhookLogId(webhookResponseFromADIS.get().getProductWebhookLogId());
                productWebhook.setRetryCount(webhookResponseFromADIS.get().getRetryCount());
            } else {
                log.info("WebHook data contains Product Create data");
                productWebhook.setRetryCount(0);
            }
            productWebhook.setStorehash(secrets.getStorehash());

            productWebhookRepo.save(productWebhook);
            log.info("BCOrderService.storeQueueResponseInAdis method ended");
            return true;
        } catch (Exception e) {
            log.error("Exception Occured on method ordercommons.BCOrderService.storeQueueResponseInAdis  :" + e.toString());
            //emailService.sendErrorMail("ordercommons.BCOrderService.storeQueueResponseInAdis", e.getMessage(), secrets.getClientName() + OCConstants.developerMailSub);
            return false;
        }

    }
    
    public boolean storeCategoryQueueResponseInAdis(ProductWebhookDTO queueResponse, String status) {
        try {
            log.info("BCOrderService.storeCategoryQueueResponseInAdis method started");
            PCCategoryWebhookLogTransaction productWebhook = new PCCategoryWebhookLogTransaction();
            Optional<PCCategoryWebhookLogTransaction> webhookResponseFromADIS = categoryWebhookRepo.findByDestinationCategoryIdAndStorehash(queueResponse.getData().getId(), secrets.getStorehash());
            //orderWebhook.setDestinationOrderId(queueResponse.getData().getId());
            productWebhook.setDestinationCategoryId(queueResponse.getData().getId());
            productWebhook.setScope(queueResponse.getScope());
            productWebhook.setStatus(status);
            if (webhookResponseFromADIS.isPresent()) {
                log.info("WebHook data contains Category Update data");
                productWebhook.setCategoryWebhookLogId(webhookResponseFromADIS.get().getCategoryWebhookLogId());
                productWebhook.setRetryCount(webhookResponseFromADIS.get().getRetryCount());
            } else {
                log.info("WebHook data contains Category Create data");
                productWebhook.setRetryCount(0);
            }
            productWebhook.setStorehash(secrets.getStorehash());
            categoryWebhookRepo.save(productWebhook);
            log.info("BCOrderService.storeQueueResponseInAdis method ended");
            return true;
        } catch (Exception e) {
            log.error("Exception Occured on method productcommons.storeCategoryQueueResponseInAdis  :" + e.toString());
            //emailService.sendErrorMail("ordercommons.BCOrderService.storeQueueResponseInAdis", e.getMessage(), secrets.getClientName() + OCConstants.developerMailSub);
            return false;
        }

    }
    
    public String fetchFiltersFromBC() {
        try {
            restService.fetchSearchContextFilters();
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception occured when integrating context filters from BC to ADIS" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
    }
    
    
}
