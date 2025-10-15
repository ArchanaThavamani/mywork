/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.service;

import com.arizon.productcommon.config.PCConstants;

import static com.arizon.productcommon.config.PCConstants.INPROGRESS;
import static com.arizon.productcommon.config.PCConstants.secrets;

import com.arizon.productcommon.config.PCProductCommonConfig;
import com.arizon.productcommon.entity.*;
import com.arizon.productcommon.model.*;
import com.arizon.productcommon.repository.PCBrandTransactionLogRepository;
import com.arizon.productcommon.repository.PCBrandTransactionRepository;
import com.arizon.productcommon.repository.PCProductCategoryMappingRepo;
import com.arizon.productcommon.repository.PCCategoryTransactionLogRepository;
import com.arizon.productcommon.repository.PCCategoryTransactionRepository;
import com.arizon.productcommon.repository.PCContextFilterRepository;
import com.arizon.productcommon.repository.PCProductCustomFeildTransactionLogRepository;
import com.arizon.productcommon.repository.PCProductCustomFieldTransactionRepository;
import com.arizon.productcommon.repository.PCProductImageTransactionRepository;
import com.arizon.productcommon.repository.PCProductOptionLogRepository;
import com.arizon.productcommon.repository.PCProductOptionSkuBackOrderDetailTransactionRepository;
import com.arizon.productcommon.repository.PCProductOptionSkuDetailsTransactionRepository;
import com.arizon.productcommon.repository.PCProductOptionSkuLogRepository;
import com.arizon.productcommon.repository.PCProductOptionSkuTransactionRespository;
import com.arizon.productcommon.repository.PCProductOptionTransactionRepository;
import com.arizon.productcommon.repository.PCProductOptionValueLogRespository;
import com.arizon.productcommon.repository.PCProductOptionValueTransactionRepository;
import com.arizon.productcommon.repository.PCProductTransactionLogRepository;
import com.arizon.productcommon.repository.PCProductTransactionRepository;
import com.arizon.productcommon.util.PCAWSBucketService;
import com.arizon.productcommon.util.ProductUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author mohan.e
 */
@Service
@Slf4j
public class PCAdisLogService {

    @Autowired
    PCProductTransactionRepository PCProductTransactionRepository;

    @Autowired
    PCProductTransactionLogRepository PCProductTransactionLogRepository;

    @Autowired
    PCCategoryTransactionRepository categoryTransactionRepository;

    @Autowired
    PCProductCategoryMappingRepo productCategoryMapRepo;

    @Autowired
    PCProductCustomFieldTransactionRepository customFeildTransactionRepository;

    @Autowired
    PCProductCustomFeildTransactionLogRepository customFeildTransactionLogRepository;

    @Autowired
    PCBrandTransactionRepository pcBrandTransactionRepository;

    @Autowired
    PCBrandTransactionLogRepository brandTransactionLogRepository;

    @Autowired
    PCCategoryTransactionLogRepository categoryLogRepo;

    @Autowired
    private PCProductOptionSkuLogRepository optionSkuLogRepo;

    @Autowired
    PCProductOptionTransactionRepository productOptionRepo;

    @Autowired
    private PCProductOptionLogRepository optionLogRepo;

    @Autowired
    PCProductOptionValueTransactionRepository productOptionValueRepo;
    @Autowired
    private PCProductOptionValueLogRespository optionValueLogRepo;

    @Autowired
    private PCAWSBucketService s3Service;

    @Autowired
    PCProductEmailService emailService;

    @Autowired
    PCProductCommonConfig config;

    @Autowired
    PCProductOptionSkuTransactionRespository productOptionSkuRepo;

    @Autowired
    PCProductOptionSkuDetailsTransactionRepository productOptionSkuDetailsRepo;

    @Autowired
    PCProductOptionSkuLogRepository productOptionSkuLog;

    @Autowired
    PCProductOptionSkuBackOrderDetailTransactionRepository productOptionSkuBackOrderDetailRepo;

    @Autowired
    PCProductImageTransactionRepository productImageRepo;
    
    @Autowired
    PCContextFilterRepository contextFilterRepo;
    
//    @Autowired
//    PCBigcommerceRestService restService;

    public int productStatusUpdate(int productId, String Status, ProductDTO productdata) {
        log.info("AdisToBigCommerceService productStatusUpdate method started");
        try {
            log.info("productStatusUpdate method started :" + productId + " Status :" + Status);
            Optional<PCProductTransaction> optionalProductdata = PCProductTransactionRepository.findByProductId(productId);
            PCProductTransaction productTransaction = optionalProductdata.get();

            if (Status.equalsIgnoreCase(INPROGRESS)) {

                productTransaction.setDestinationProductId(productdata.getId());
                productTransaction.setInventoryLevel(productdata.getInventory_level());
                productTransaction.setPrice(BigDecimal.valueOf(productdata.getPrice()));
                productTransaction.setInventoryWarningLevel(productdata.getInventory_warning_level());
                if (productdata.getCustom_url() != null) {
                    productTransaction.setCustomUrl(productdata.getCustom_url().getUrl());
                    productTransaction.setCustomized(productdata.getCustom_url().is_customized);
                }
                productTransaction.setAvailability(productdata.getAvailability());
                productTransaction.setCalculatedPrice(productdata.getCalculated_price());
                productTransaction.setCondition(productdata.getCondition());
                productTransaction.setConditionShown(productdata.is_condition_shown);
                productTransaction.setTaxClassId(productdata.getTax_class_id());
                productTransaction.setTotalSold(productdata.getTotal_sold());
                productTransaction.setType(productdata.getType());
                productTransaction.setViewCount(productdata.getView_count());
                productTransaction.setVisible(productdata.is_visible);
                productTransaction.setDestination_option_set_id(productdata.getOption_set_id());
                productTransaction.setFeatured(productdata.is_featured);
                productTransaction.setFixedCostShippingPrice(BigDecimal.valueOf(productdata.getFixed_cost_shipping_price()));
                productTransaction.setFreeShipping(productdata.is_free_shipping);
                productTransaction.setPreorderOnly(productdata.is_preorder_only);
                productTransaction.setPriceHidden(productdata.is_price_hidden);
                productTransaction.setReviewsCount(productdata.getReviews_count());
                productTransaction.setReviewsRatingSum(productdata.getReviews_rating_sum());
                productTransaction.setSortOrder(productdata.getSort_order());
                productTransaction.setGiftWrappingOptionsType(productdata.getGift_wrapping_options_type());
                productTransaction.setOrderQuantityMaximum(productdata.getOrder_quantity_maximum());
                productTransaction.setOrderQuantityMinimum(productdata.getOrder_quantity_minimum());
                // productTransaction.setBrandId(productdata.getBrand_id());
                productTransaction.setOpenGraphType(productdata.getOpen_graph_type());
                productTransaction.setOpenGraphUseImage(productdata.getOpen_graph_use_image());
                productTransaction.setOpenGraphUseMetaDescription(productdata.getOpen_graph_use_meta_description());
                productTransaction.setOpenGraphUseProductName(productdata.getOpen_graph_use_product_name());

                productTransaction.setProductStatus(PCConstants.SUCCESS);
                PCProductTransactionRepository.save(productTransaction);
                return productdata.getId();
            } else if (Status.equalsIgnoreCase(PCConstants.DELETE)) {
                productTransaction.setProductStatus(PCConstants.SUCCESS);
                PCProductTransactionRepository.save(productTransaction);
                return productdata.getId();
            } else {
                productTransaction.setProductStatus(PCConstants.FAILED);
                PCProductTransactionRepository.save(productTransaction);
                return 0;
            }

        } catch (Exception Ex) {
            log.error("Exception occurs in productStatusUpdate :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "productStatusUpdate", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.ZEROS;
        }
    }

  
    public String bcProductOneTimeSync(int bcProductID, ProductDTO productdata) {
        PCProductTransaction productTransaction = new PCProductTransaction();
        try {
            log.info("AdisLogService -> bcProductOneTimeSync method started");
            PCProductTransaction product = null;
            productTransaction.setStorehash(secrets.getStorehash());
            productTransaction.setDestinationProductId(productdata.getId());
            productTransaction.setName(productdata.getName());
            productTransaction.setSku(productdata.getSku());
            productTransaction.setWeight(productdata.getWeight());
            productTransaction.setDepth(productdata.getDepth());
            productTransaction.setHeight(productdata.getHeight());
            productTransaction.setCostPrice(BigDecimal.valueOf(productdata.getCost_price()));
            productTransaction.setProductTaxCode(productdata.getProduct_tax_code());
            productTransaction.setInventoryTracking(productdata.getInventory_tracking());
            productTransaction.setWarranty(productdata.getWarranty());
            productTransaction.setBinPickingNumber(productdata.getBin_picking_number());
            productTransaction.setUpc(productdata.getUpc());
            productTransaction.setLayoutFile(productdata.getLayout_file());
            productTransaction.setSearchKeywords(productdata.getSearch_keywords());
            productTransaction.setAvailabilityDescription(productdata.getAvailability_description());
            productTransaction.setRetailPrice(BigDecimal.valueOf(productdata.getRetail_price()));
            productTransaction.setSalePrice(BigDecimal.valueOf(productdata.getSale_price()));
            productTransaction.setPageTitle(productdata.getPage_title());
            productTransaction.setMetaDescription(productdata.getMeta_description());
            productTransaction.setBrandName(productdata.getBrand_name());
            productTransaction.setGtin(productdata.getGtin());
            productTransaction.setMpn(productdata.getMpn());
            productTransaction.setInventoryLevel(productdata.getInventory_level());
            productTransaction.setPrice(BigDecimal.valueOf(productdata.getPrice()));
            productTransaction.setInventoryWarningLevel(productdata.getInventory_warning_level());
            if (productdata.getCustom_url() != null) {
                productTransaction.setCustomUrl(productdata.getCustom_url().getUrl() != null ? productdata.getCustom_url().getUrl() : "");
                productTransaction.setCustomized(productdata.getCustom_url().is_customized != null ? productdata.getCustom_url().is_customized : false);
            } else {
                productTransaction.setCustomUrl("");
                productTransaction.setCustomized(false); 
            }
            productTransaction.setAvailability(productdata.getAvailability());
            productTransaction.setCalculatedPrice(productdata.getCalculated_price());
            productTransaction.setCondition(productdata.getCondition());
            productTransaction.setConditionShown(productdata.is_condition_shown);
            productTransaction.setTaxClassId(productdata.getTax_class_id());
            productTransaction.setTotalSold(productdata.getTotal_sold());
            productTransaction.setType(productdata.getType());
            productTransaction.setViewCount(productdata.getView_count());
            productTransaction.setVisible(productdata.is_visible);
            productTransaction.setDestination_option_set_id(productdata.getOption_set_id());
            productTransaction.setFeatured(productdata.is_featured);
            productTransaction.setFixedCostShippingPrice(BigDecimal.valueOf(productdata.getFixed_cost_shipping_price()));
            productTransaction.setFreeShipping(productdata.is_free_shipping);
            productTransaction.setPreorderOnly(productdata.is_preorder_only);
            productTransaction.setPriceHidden(productdata.is_price_hidden);
            productTransaction.setReviewsCount(productdata.getReviews_count());
            productTransaction.setReviewsRatingSum(productdata.getReviews_rating_sum());
            productTransaction.setSortOrder(productdata.getSort_order());
            productTransaction.setGiftWrappingOptionsType(productdata.getGift_wrapping_options_type());
            productTransaction.setOrderQuantityMaximum(productdata.getOrder_quantity_maximum());
            productTransaction.setOrderQuantityMinimum(productdata.getOrder_quantity_minimum());
            productTransaction.setBrandId(productdata.getBrand_id());
            productTransaction.setOpenGraphType(productdata.getOpen_graph_type());
            productTransaction.setDescription(productdata.getDescription());
            productTransaction.setOpenGraphUseImage(productdata.getOpen_graph_use_image());
            productTransaction.setOpenGraphUseMetaDescription(productdata.getOpen_graph_use_meta_description());
            productTransaction.setOpenGraphUseProductName(productdata.getOpen_graph_use_product_name());
            Optional<PCProductTransaction> optionalProductdata = PCProductTransactionRepository.findByStorehashAndDestinationProductId(secrets.getStorehash(), bcProductID);
            if (optionalProductdata.isPresent()) {
            	if(PCConstants.SUCCESS.equals(optionalProductdata.get().getProductStatus())) {
            		productTransaction.setProductStatus(PCConstants.UPDATE);
            	} else {
            		productTransaction.setProductStatus(PCConstants.PENDING);
            	}
                productTransaction.setSourceProductId(optionalProductdata.get().getSourceProductId() != null ? optionalProductdata.get().getSourceProductId() : null);
                productTransaction.setProductId(optionalProductdata.get().getProductId());
                product = PCProductTransactionRepository.save(productTransaction);
            } else {
                productTransaction.setProductStatus(PCConstants.PENDING);
                product = PCProductTransactionRepository.save(productTransaction);
            }

            storeProductOptionSkuData(product.getProductId(), productdata);
            storeProductOptionData(product.getProductId(), productdata);
            storeCustomFieldsDetails(product.getProductId(), productdata.getId(), productdata.getCustom_fields());
            storeProductImageDetails(product.getProductId(), productdata.getImages());
            oneTimeSyncProductCategoryMapping(product.getProductId(), productdata.getCategories());
        } catch (Exception e) {
            log.error("Exception occured when stored product data in ADIS table from BC" + ProductUtility.getStackTrace(e));
            insertProductLog(0, PCConstants.FAILED, productTransaction.getProductStatus(), "", ProductUtility.getStackTrace(e), bcProductID);
            return PCConstants.FAILED;
        }
        log.info("AdisLogService -> bcProductOneTimeSync method ended");
        return PCConstants.SUCCESS;
    }

	public String insertFilters(ContextFilterResponseDTO contextFilterResponse) {
		try {
			log.info("insertFilters method started");
			List<ContextFilterDataDTO> contextFilters = contextFilterResponse.getData();
			for (ContextFilterDataDTO contextFilter : contextFilters) {
				storeContextFilter(contextFilter);
			}
			return PCConstants.SUCCESS;
		} catch (Exception ex) {
			log.error("Exception occurs in insertFilters :" + ProductUtility.getStackTrace(ex));
			return PCConstants.FAILED;
		}

	}
    
	public String storeContextFilter(ContextFilterDataDTO contextFilter) {
		try {
			log.info("storeContextFilter method started");;
			List<FilterDTO> filters = contextFilter.getData();
			for (FilterDTO filter : filters) {
				PCContextFilters contextFilterEntity = new PCContextFilters();
				contextFilterEntity.setDestinationCategoryId(contextFilter.getContext().getCategoryId());
				contextFilterEntity.setDisplayName(filter.getDisplayName());
				contextFilterEntity.setChannelId(contextFilter.getContext().getChannelId());
				contextFilterEntity.setType(filter.getType());
				contextFilterEntity.setDestinationFilterId(filter.getId());
				contextFilterEntity.setFacetId(filter.getFacetId()!=null?filter.getFacetId():0);
				contextFilterEntity.setFacetKey(filter.getFacet()!=null?filter.getFacet():"");
				contextFilterEntity.setDisplayCount(filter.getDisplayProductCount()!=null?filter.getDisplayProductCount():false);
				contextFilterEntity.setItemCount(filter.getItemsToShow()!=null?filter.getItemsToShow():0);
				contextFilterEntity.setCollapse(filter.getCollapsedByDefault()!=null?filter.getCollapsedByDefault():false);
				contextFilterEntity.setIsActive(true);
				contextFilterEntity.setIsDeleted(false);
				Optional<PCContextFilters> optionalfilter = contextFilterRepo.findByDestinationCategoryIdAndTypeAndDisplayName(contextFilterEntity.getDestinationCategoryId(), contextFilterEntity.getType() , contextFilterEntity.getDisplayName());
				if(optionalfilter.isPresent()) {
					contextFilterEntity.setContextFilterId(optionalfilter.get().getContextFilterId());
					contextFilterRepo.save(contextFilterEntity);
				} else {
					contextFilterRepo.save(contextFilterEntity);
				}
				
			}
			return PCConstants.SUCCESS;
		} catch (Exception ex) {
			log.error("Exception occurs in storeContextFilter :" + ProductUtility.getStackTrace(ex));
			return PCConstants.FAILED;
		}
	}
    
    public void insertProductLog(int productId, String status, String Type, String request, String response, int desProductID) {
        try {
            log.info("AdisLogService.insertProductLog method started");
            PCProductTransactionLog productlog = new PCProductTransactionLog();
            productlog.setProductId(productId);
            productlog.setType(Type);
            productlog.setStatus(status);
            productlog.setRequest(request);
            productlog.setResponse(response);
            productlog.setDestinationProductId(desProductID);
            PCProductTransactionLogRepository.save(productlog);
        } catch (Exception Ex) {
            log.error("Exception occurs in insertProductLog :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "insertProductLog", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public void CategoryToAdis(CategoryResponseDTO categoryResponse, PCCategoryTransaction PCCategoryTransaction, String status) {
        try {
            if (status.equalsIgnoreCase(PCConstants.SUCCESS)) {
                CategoryDataDTO category = categoryResponse.getData().get(0);
                PCCategoryTransaction.setDestinationParentCategoryId(category.getParent_id());
                PCCategoryTransaction.setDestinationCategoryId(category.getId());
                PCCategoryTransaction.setName(category.getName());
                PCCategoryTransaction.setDescription(category.getDescription());
                PCCategoryTransaction.setLayoutFile(category.getLayout_file());
                PCCategoryTransaction.setImageUrl(category.getImage_url());
                PCCategoryTransaction.setCustomUrl(category.getCustom_url() != null ? category.getCustom_url().getUrl() : null);
                PCCategoryTransaction.setDefaultProductSort(category.getDefault_product_sort());
                PCCategoryTransaction.setStatus(status);
                PCCategoryTransaction.setActive(true);
                categoryTransactionRepository.save(PCCategoryTransaction);
            } else if (status.equalsIgnoreCase(PCConstants.DELETE)) {
                PCCategoryTransaction.setActive(false);
                PCCategoryTransaction.setStatus(PCConstants.SUCCESS);
                categoryTransactionRepository.save(PCCategoryTransaction);
            } else {
                PCCategoryTransaction.setStatus(status);
                PCCategoryTransaction.setActive(true);
                categoryTransactionRepository.save(PCCategoryTransaction);
            }
        } catch (Exception Ex) {
            log.error("Exception occured in : CategoryToAdis" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public void brandOneTimeSyncToAdis(BrandResponseDTO brandResponseDTO) {
        try {
            log.info("AdisLogService -> brandOneTimeSyncToAdis method started");
            List<BrandDataDTO> brandData = brandResponseDTO.getData();
            storeBrandDatatoADIS(brandData);
        } catch (Exception Ex) {
            log.error("Exception occured in : brandOneTimeSyncToAdis" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "brandOneTimeSyncToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService -> brandOneTimeSyncToAdis method ended");
    }


    public void CategoryOneTimeSyncToAdis(CategoryResponseDTO categoryResponse) {
        try {
            log.info("AdisLogService -> CategoryOneTimeSyncToAdis method started");
            List<CategoryDataDTO> categoryData = categoryResponse.getData();
            int parentCategoryId = 0;
            storeCategoryDatatoADIS(categoryData);
            //          List<CategoryDataDTO> subcategory = categoryData.stream()
            //                  .filter(categoryDatas -> categoryDatas.getParent_id() == parentCategoryId)
            //                  .collect(Collectors.toList());
            //         log.info("Subcategory:" + subcategory);
        } catch (Exception Ex) {
            log.error("Exception occured in : CategoryOneTimeSyncToAdis" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService -> CategoryOneTimeSyncToAdis method ended");
    }
    
    public void CategoryTreeOneTimeSyncToAdis(CategoryTreeResponseDTO categoryResponse) {
        try {
            log.info("AdisLogService -> CategoryOneTimeSyncToAdis method started");
            List<CategoryTreeData> categoryData = categoryResponse.getData();
            int parentCategoryId = 0;
            storeCategoryTreeDatatoADIS(categoryData);
            //          List<CategoryDataDTO> subcategory = categoryData.stream()
            //                  .filter(categoryDatas -> categoryDatas.getParent_id() == parentCategoryId)
            //                  .collect(Collectors.toList());
            //         log.info("Subcategory:" + subcategory);
        } catch (Exception Ex) {
            log.error("Exception occured in : CategoryOneTimeSyncToAdis" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService -> CategoryOneTimeSyncToAdis method ended");
    }
    
    public void CategorySyncByIdToAdis(CategoryTreeResponseDTO categoryResponse) {
        try {
            log.info("AdisLogService -> CategoryOneTimeSyncToAdis method started");
            List<CategoryTreeData> categoryData = categoryResponse.getData();
            if(!categoryData.isEmpty()) {
              storeSingleCategoryToAdis(categoryData.get(0));
            }  
        } catch (Exception Ex) {
            log.error("Exception occured in : CategoryOneTimeSyncToAdis" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService -> CategorySyncByIdToAdis method ended");
    }

//    public void CategoryTreeOneTimeSyncToAdis(CategoryTreeResponseDTO categoryResponse) {
//        try {
//            log.info("AdisLogService -> CategoryOneTimeSyncToAdis method started");
//            List<CategoryTreeData> categoryData = categoryResponse.getData();
//            for (CategoryTreeData category : categoryData) {
//                Optional<PCCategoryTransaction> CheckCategoryPresence = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getCategory_id(), secrets.getStorehash() ,true, false);
//                if (CheckCategoryPresence.isEmpty()) {
//                    log.info("The received category starts to insert data into ADIS " + category.getName());
//                    PCCategoryTransaction categoryObj = new PCCategoryTransaction();
//                    categoryObj.setDestinationCategoryId(category.getCategory_id());
//                    if (category.getParent_id() != 0) {
//                        categoryObj.setDestinationParentCategoryId(category.getParent_id());
//                        Optional<PCCategoryTransaction> categoryParentData = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getParent_id(),secrets.getStorehash() ,true, false);
//                        if (categoryParentData.isPresent()) {
//                        	PCCategoryTransaction parentData = categoryParentData.get();
//                            categoryObj.setParentCategoryId(parentData.getCategoryId());
//                                            String path = parentData.getCategoryPath() + "/" + category.getName();
//                                           categoryObj.setCategoryPath(path);
//                        }
//                    }
//                                  else {
//                                        categoryObj.setCategoryPath(category.getName());
//                                        categoryObj.setDestinationParentCategoryId(0);
//                                    }
//                    categoryObj.setName(category.getName());
//                    if (category.getDescription() != null && category.getDescription().isEmpty()) {
//                        categoryObj.setDescription(category.getDescription());
//                    }
//                    categoryObj.setViews(category.getViews());
//                    categoryObj.setSortOrder(category.getSort_order());
//                    if (category.getLayout_file() != null && !category.getLayout_file().isEmpty()) {
//                        categoryObj.setLayoutFile(category.getLayout_file());
//                    }
//                    if (category.getSearch_keywords() != null && !category.getSearch_keywords().isEmpty()) {
//                        categoryObj.setSearchKeywords(category.getSearch_keywords());
//                    }
//                    if (category.getImage_url() != null && !category.getImage_url().isEmpty()) {
//                        categoryObj.setImageUrl(category.getImage_url());
//                    }
//                    if (category.getPage_title() != null && !category.getPage_title().isEmpty()) {
//                        categoryObj.setPageTitle(category.getPage_title());
//                    }
//                    categoryObj.setDefaultProductSort(category.getDefault_product_sort());
//                    categoryObj.setCustomUrl(category.getCustom_url().getUrl());
//                    categoryObj.setActive(true);
//                    categoryObj.setDeleted(false);
//                    categoryObj.setStatus(PCConstants.INSERT);
//                    //categoryObj.setTreeId(category.getTree_id());
//                    PCCategoryTransaction categoryResponseData = categoryTransactionRepository.save(categoryObj);
//                    log.info("Category insert response : " + categoryResponseData);
//                } else {
//                    log.info("The received Category is already exist in ADIS and It starts to Update " + category.getName());
//                    PCCategoryTransaction categoryObj = CheckCategoryPresence.get();
//                    if (category.getParent_id() != 0) {
//                        categoryObj.setDestinationParentCategoryId(category.getParent_id());
//                        Optional<PCCategoryTransaction> categoryParentData = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getParent_id(),secrets.getStorehash(),true, false);
//                        if (categoryParentData.isPresent()) {
//                        	PCCategoryTransaction parentData = categoryParentData.get();
//                            categoryObj.setParentCategoryId(parentData.getCategoryId());
////                                            String path = parentData.getCategoryPath() + "/" + category.getName();
////                                            categoryObj.setCategoryPath(path);
//                        }
//                    }
////                                    else {
////                                        categoryObj.setCategoryPath(category.getName());
////                                        categoryObj.setDestinationParentCategoryId(0);
////                                    }
//                    categoryObj.setName(category.getName());
//                    if (category.getDescription() != null && !category.getDescription().isEmpty()) {
//                        categoryObj.setDescription(category.getDescription());
//                    }
//                    categoryObj.setViews(category.getViews());
//                    categoryObj.setSortOrder(category.getSort_order());
//                    if (category.getLayout_file() != null && !category.getLayout_file().isEmpty()) {
//                        categoryObj.setLayoutFile(category.getLayout_file());
//                    }
//                    if (category.getSearch_keywords() != null && !category.getSearch_keywords().isEmpty()) {
//                        categoryObj.setSearchKeywords(category.getSearch_keywords());
//                    }
//                    if (category.getImage_url() != null && !category.getImage_url().isEmpty()) {
//                        categoryObj.setImageUrl(category.getImage_url());
//                    }
//                    if (category.getPage_title() != null && !category.getPage_title().isEmpty()) {
//                        categoryObj.setPageTitle(category.getPage_title());
//                    }
//                    categoryObj.setActive(true);
//                    categoryObj.setDeleted(false);
//                    categoryObj.setStatus(PCConstants.UPDATE);
//                    categoryObj.setDefaultProductSort(category.getDefault_product_sort());
//                    categoryObj.setCustomUrl(category.getCustom_url().getUrl());
//                    //categoryObj.setTreeId(category.getTree_id());
//                    PCCategoryTransaction categoryResponseData = categoryTransactionRepository.save(categoryObj);
//                    log.info("Category update response : " + categoryResponseData);
//
//                }
//            }
//        } catch (Exception Ex) {
//            log.error("Exception occured in : CategoryOneTimeSyncToAdis" + ProductUtility.getStackTrace(Ex));
//            emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
//        }
//        log.info("AdisLogService -> CategoryOneTimeSyncToAdis method ended");
//    }

    
    public String storeBrandDatatoADIS(List<BrandDataDTO> brandDataDTOList) {
        try {
            log.info("AdisLogService -> storeBrandDatatoADIS method started");
            for (BrandDataDTO brandDataDTO : brandDataDTOList) {
                PCBrandTransaction pcBrandTransaction = new PCBrandTransaction();
                pcBrandTransaction.setDestinationBrandId(brandDataDTO.getId());
                pcBrandTransaction.setStorehash(secrets.getStorehash());
                pcBrandTransaction.setName(brandDataDTO.getName());
                pcBrandTransaction.setPageTitle(brandDataDTO.getPage_title());
                pcBrandTransaction.setMetaDescription(brandDataDTO.getMeta_description());
                pcBrandTransaction.setImageUrl(brandDataDTO.getImage_url());
                pcBrandTransaction.setCustomUrl(brandDataDTO.getCustom_url().getUrl());
                //pcBrandTransaction.setCustomized(brandDataDTO.getCustom_url());
                pcBrandTransaction.setActive(true);
                Optional<PCBrandTransaction> pcBrandTransaction1 = pcBrandTransactionRepository.findByStorehashAndDestinationBrandId(secrets.getStorehash(), brandDataDTO.getId());
                if (pcBrandTransaction1.isPresent()) {
                    pcBrandTransaction.setStatus(PCConstants.UPDATE);
                    pcBrandTransaction.setBrandId(pcBrandTransaction1.get().getBrandId());
                    pcBrandTransaction = pcBrandTransactionRepository.save(pcBrandTransaction);
                } else {
                    pcBrandTransaction.setStatus(PCConstants.INSERT);
                    pcBrandTransaction = pcBrandTransactionRepository.save(pcBrandTransaction);
                }

            }
            log.info("AdisLogService -> storeBrandDatatoADIS method ended");
            return PCConstants.SUCCESS;
        } catch (Exception Ex) {
            log.error("Exception occured in : storeBrandDatatoADIS" + ProductUtility.getStackTrace(Ex));
            return PCConstants.FAILED;
        }
    }

    public String storeCategoryDatatoADIS(List<CategoryDataDTO> categoryData) {
        try {
            log.info("AdisLogService -> storeCategoryDatatoADIS method started");
            for (int i = 0; i < categoryData.size(); i++) {
                CategoryDataDTO category = categoryData.get(i);
                PCCategoryTransaction PCCategoryTransaction = new PCCategoryTransaction();
                PCCategoryTransaction.setStorehash(secrets.getStorehash());
                PCCategoryTransaction.setDestinationCategoryId(category.getId());
                PCCategoryTransaction.setDestinationParentCategoryId(category.getParent_id());
                PCCategoryTransaction.setName(category.getName());
                PCCategoryTransaction.setDescription(category.getDescription());
                PCCategoryTransaction.setViews(category.getViews());
                PCCategoryTransaction.setSortOrder(category.getSort_order());
                PCCategoryTransaction.setPageTitle(category.getPage_title());
                PCCategoryTransaction.setMetaKeywords(category.getMeta_keywords() != null ? category.getMeta_keywords().toString() : null);
                PCCategoryTransaction.setMetaDescription(category.getMeta_description());

                PCCategoryTransaction.setLayoutFile(category.getLayout_file());
                PCCategoryTransaction.setImageUrl(category.getImage_url());
                PCCategoryTransaction.setCustomUrl(category.getCustom_url() != null ? category.getCustom_url().getUrl() : null);
                PCCategoryTransaction.setDefaultProductSort(category.getDefault_product_sort().toString());
                PCCategoryTransaction.setSearchKeywords(category.getSearch_keywords());
                PCCategoryTransaction.setVisible(category.is_visible());
                PCCategoryTransaction.setActive(true);

                Optional<PCCategoryTransaction> productParentCategory = categoryTransactionRepository.findFirstByDestinationCategoryIdAndStorehash(PCCategoryTransaction.getDestinationParentCategoryId(), secrets.getStorehash());
                if (productParentCategory.isPresent()) {
                    PCCategoryTransaction.setParentCategoryId(productParentCategory.get().getCategoryId());
                }

                Optional<PCCategoryTransaction> productCategory = categoryTransactionRepository.findByStorehashAndDestinationCategoryId(secrets.getStorehash(), category.getId());
                if (productCategory.isPresent()) {
                    PCCategoryTransaction.setStatus(PCConstants.UPDATE);
                    PCCategoryTransaction.setCategoryId(productCategory.get().getCategoryId());
                    PCCategoryTransaction = categoryTransactionRepository.save(PCCategoryTransaction);
                } else {
                    PCCategoryTransaction.setStatus(PCConstants.INSERT);
                    PCCategoryTransaction = categoryTransactionRepository.save(PCCategoryTransaction);
                }

                List<PCCategoryTransaction> productChildCategoryList = categoryTransactionRepository.findByDestinationParentCategoryIdAndStorehashAndParentCategoryIdIsNull(PCCategoryTransaction.getDestinationCategoryId(), secrets.getStorehash());

                for(PCCategoryTransaction pcCategoryTransaction : productChildCategoryList) {
                    pcCategoryTransaction.setParentCategoryId(PCCategoryTransaction.getCategoryId());
                }

                categoryTransactionRepository.saveAll(productChildCategoryList);

            }
            log.info("AdisLogService -> storeCategoryDatatoADIS method ended");
            return PCConstants.SUCCESS;
		} catch (Exception Ex) {
			log.error("Exception occured in : storeCategoryDatatoADIS" + ProductUtility.getStackTrace(Ex));
			return PCConstants.FAILED;
			// emailService.sendErrorMail(secrets.getClientName(), "CategoryToAdis",
			// Utility.getStackTrace(Ex), Constants.DEVELOPER_MAIL_SUB, config.getPrefix());
		}
    }
    
	public String storeSingleCategoryToAdis(CategoryTreeData category) {
		try {
			PCCategoryTransaction categoryTransaction = new PCCategoryTransaction();

			categoryTransaction.setDestinationCategoryId(category.getCategory_id());
			categoryTransaction.setDestinationParentCategoryId(category.getParent_id());
			categoryTransaction.setName(category.getName());
			categoryTransaction.setDescription(category.getDescription());
			categoryTransaction.setViews(category.getViews());
			categoryTransaction.setSortOrder(category.getSort_order());
			categoryTransaction.setLayoutFile(category.getLayout_file());
			categoryTransaction.setImageUrl(category.getImage_url());
			categoryTransaction.setPageTitle(category.getPage_title());
			categoryTransaction.setDefaultProductSort(category.getDefault_product_sort());
			categoryTransaction.setCustomUrl(category.getCustom_url() != null ? category.getCustom_url().getPath() : null);
			categoryTransaction.setSearchKeywords(category.getSearch_keywords());
			categoryTransaction.setActive(true);
			categoryTransaction.setDeleted(false);
			categoryTransaction.setStorehash(secrets.getStorehash());
			// categoryTransaction.setTreeId(category.getTree_id());

			Optional<PCCategoryTransaction> parentCategoryOpt = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getParent_id(),secrets.getStorehash(), true, false);
			parentCategoryOpt.ifPresent(parent -> categoryTransaction.setParentCategoryId(parent.getCategoryId()));

			Optional<PCCategoryTransaction> existingCategoryOpt = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getCategory_id(),secrets.getStorehash(), true, false);

			if (existingCategoryOpt.isPresent()) {
				categoryTransaction.setCategoryId(existingCategoryOpt.get().getCategoryId());
				if(!PCConstants.PENDING.equals(categoryTransaction.getStatus())) {
					categoryTransaction.setStatus(PCConstants.UPDATE);
				}	
			} else {

				categoryTransaction.setStatus(PCConstants.PENDING);
			}

			PCCategoryTransaction saved = categoryTransactionRepository.save(categoryTransaction);

			List<PCCategoryTransaction> parentschildcategories = categoryTransactionRepository.findByDestinationParentCategoryIdAndStorehashAndParentCategoryIdIsNull(saved.getDestinationCategoryId(), secrets.getStorehash());

			for (PCCategoryTransaction child : parentschildcategories) {
				child.setParentCategoryId(saved.getCategoryId());
			}

			categoryTransactionRepository.saveAll(parentschildcategories);
			log.info("AdisLogService -> storeCategoryTreeDatatoADIS method ended");
			return PCConstants.SUCCESS;
		} catch (Exception ex) {
			log.error("Exception occurred in storeSingleCategoryTreeDatatoADIS: " + ProductUtility.getStackTrace(ex));
			return PCConstants.FAILED;
		}
	}
    
    public String storeCategoryTreeDatatoADIS(List<CategoryTreeData> categoryDataList) {
        try {
            log.info("AdisLogService -> storeCategoryTreeDatatoADIS method started");

            for (CategoryTreeData category : categoryDataList) {
            	PCCategoryTransaction categoryTransaction = new PCCategoryTransaction();

                categoryTransaction.setDestinationCategoryId(category.getCategory_id());
                categoryTransaction.setDestinationParentCategoryId(category.getParent_id());
                categoryTransaction.setName(category.getName());
                categoryTransaction.setDescription(category.getDescription());
                categoryTransaction.setViews(category.getViews());
                categoryTransaction.setSortOrder(category.getSort_order());
                categoryTransaction.setLayoutFile(category.getLayout_file());
                categoryTransaction.setImageUrl(category.getImage_url());
                categoryTransaction.setPageTitle(category.getPage_title());
                categoryTransaction.setDefaultProductSort(category.getDefault_product_sort());
                categoryTransaction.setCustomUrl(category.getCustom_url() != null ? category.getCustom_url().getPath() : null);
                categoryTransaction.setSearchKeywords(category.getSearch_keywords());
                categoryTransaction.setActive(true);
                categoryTransaction.setDeleted(false);
                categoryTransaction.setStorehash(secrets.getStorehash());;
                //categoryTransaction.setTreeId(category.getTree_id());

             
                Optional<PCCategoryTransaction> parentCategoryOpt = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getParent_id(), secrets.getStorehash() ,true, false);
                parentCategoryOpt.ifPresent(parent -> categoryTransaction.setParentCategoryId(parent.getCategoryId()));


                Optional<PCCategoryTransaction> existingCategoryOpt = categoryTransactionRepository.findByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.getCategory_id(), secrets.getStorehash(),true, false);

                if (existingCategoryOpt.isPresent()) {
                 
                	categoryTransaction.setCategoryId(existingCategoryOpt.get().getCategoryId());
    				if(!PCConstants.PENDING.equals(categoryTransaction.getStatus())) {
    					categoryTransaction.setStatus(PCConstants.UPDATE);
    				}	
                } else {
                  
                    categoryTransaction.setStatus(PCConstants.PENDING);
                }

               PCCategoryTransaction saved = categoryTransactionRepository.save(categoryTransaction);

            
                List<PCCategoryTransaction> parentschildcategories = categoryTransactionRepository.findByDestinationParentCategoryIdAndStorehashAndParentCategoryIdIsNull(saved.getDestinationCategoryId(), secrets.getStorehash());

                for (PCCategoryTransaction child : parentschildcategories) {
                    child.setParentCategoryId(saved.getCategoryId());
                }

                categoryTransactionRepository.saveAll(parentschildcategories);
            }

            log.info("AdisLogService -> storeCategoryTreeDatatoADIS method ended");
            return PCConstants.SUCCESS;

        } catch (Exception ex) {
            log.error("Exception occurred in storeCategoryTreeDatatoADIS: " + ProductUtility.getStackTrace(ex));
            // emailService.sendErrorMail(secrets.getClientName(), "CategoryTreeToAdis", Utility.getStackTrace(ex), Constants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }


//    public String oneTimeSyncProductCategoryMapping(int productId, List<Integer> category) {
//        try {
//            log.info("AdisLogService.oneTimeSyncProductCategoryMapping method started");
//            for (int i = 0; i < category.size(); i++) {
//                PCProductCategoryMapping categoryMap = new PCProductCategoryMapping();
//                categoryMap.setProductId(productId);
//                categoryMap.setActive(true);
//                categoryMap.setDeleted(false);
//                categoryMap.setStatus(PCConstants.INSERT);
//                Optional<PCCategoryTransaction> productCategory = categoryTransactionRepository.findFirstByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(category.get(i), secrets.getStorehash(),  true, false);
//                if (productCategory.isPresent()) {
//                    categoryMap.setCategoryId(productCategory.get().getCategoryId());
//                } else{
//                	String categoryFetchBCFetchstatus = restService.fetchCategoryTreeByCategoryId(category.get(i));
//                }
//                Optional<PCProductCategoryMapping> productCategoryMap = productCategoryMapRepo.findByProductIdAndCategoryIdAndIsActive(productId, categoryMap.getCategoryId() , true);
//                if (productCategoryMap.isPresent()) {
//                    categoryMap.setCategoryMappingId(productCategoryMap.get().getCategoryMappingId());
//                    categoryMap.setStatus(PCConstants.UPDATE);
//                    productCategoryMapRepo.save(categoryMap);
//                } else {
//                    categoryMap.setStatus(PCConstants.INSERT);
//                    productCategoryMapRepo.save(categoryMap);
//                }
//            }
//        } catch (Exception Ex) {
//            log.error("Exception Occured when map category details to product category :" + ProductUtility.getStackTrace(Ex));
//            return PCConstants.FAILED;
//        }
//        log.info("AdisLogService.oneTimeSyncProductCategoryMapping method ended");
//        return PCConstants.SUCCESS;
//    }
    
    public String oneTimeSyncProductCategoryMapping(int productId, List<Integer> category) {
        try {
            log.info("AdisLogService.oneTimeSyncProductCategoryMapping method started");
 
            List<PCProductCategoryMapping> existingMappings = productCategoryMapRepo
            	    .findByProductIdAndIsActiveAndIsDeleted(productId, true, false);
            
            for (PCProductCategoryMapping mapping : existingMappings) {              
                    mapping.setActive(false);
                    mapping.setDeleted(true);
                    productCategoryMapRepo.save(mapping);        
            }
 
 
            for (Integer destCategoryId : category) {
                PCProductCategoryMapping categoryMap = new PCProductCategoryMapping();
                categoryMap.setProductId(productId);
                categoryMap.setActive(true);
                categoryMap.setDeleted(false);
 
                // Try to find category in local repo
                Optional<PCCategoryTransaction> productCategory = categoryTransactionRepository.findFirstByDestinationCategoryIdAndStorehashAndIsActiveAndIsDeleted(destCategoryId, secrets.getStorehash(),  true, false);
				if (productCategory.isPresent()) {
					categoryMap.setCategoryId(productCategory.get().getCategoryId());
				} else {	
					log.info("Category ID " + destCategoryId + " not found in ADIS");
					continue;
				}
                // Check if mapping already exists
                Optional<PCProductCategoryMapping> existingMapping =
                    productCategoryMapRepo.findFirstByProductIdAndCategoryId(productId, categoryMap.getCategoryId());
 
                if (existingMapping.isPresent()) {
                    categoryMap.setCategoryMappingId(existingMapping.get().getCategoryMappingId());
                    categoryMap.setStatus(PCConstants.UPDATE);
                } else {
                    categoryMap.setStatus(PCConstants.INSERT);
                }
 
                productCategoryMapRepo.save(categoryMap);
            }
 
        } catch (Exception ex) {
            log.error("Exception occurred when mapping category details to product category: " + ProductUtility.getStackTrace(ex));
            return PCConstants.FAILED;
        }
 
        log.info("AdisLogService.oneTimeSyncProductCategoryMapping method ended");
        return PCConstants.SUCCESS;
    }
 

    public void CustomFeildToAdis(String status, PCProductCustomFieldTransaction customfeildsData, int BCProductId,
                                  int bcCustomId) {
        try {
            log.info("AdisLogService.CustomFeildToAdis method started");
            if (status.equalsIgnoreCase(PCConstants.SUCCESS)) {
                customfeildsData.setDestinationProductCustomFieldsId(bcCustomId);
                customfeildsData.setStatus(status);
                customfeildsData.setDestinationProductId(BigInteger.valueOf(BCProductId));

                customFeildTransactionRepository.save(customfeildsData);
            } else if (status.equalsIgnoreCase(PCConstants.DELETE)) {
                customfeildsData.setStatus(PCConstants.SUCCESS);
                customFeildTransactionRepository.save(customfeildsData);
            } else {
                customfeildsData.setStatus(status);
                customFeildTransactionRepository.save(customfeildsData);
            }
        } catch (Exception Ex) {
            log.error("Exception Occured in CustomFeildToAdis :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CustomFeildToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService.CustomFeildToAdis method ended");
    }

    public String storeCustomFieldsDetails(int productID, int bcProductId, ArrayList<
            CustomFieldDTO> productCustomField) {
        try {
            log.info("AdisLogService -> storeCustomFieldsDetails method started");
            if (!productCustomField.isEmpty()) {
            	
            	Set<Integer> incomingCustomFieldIds = productCustomField.stream()
        		        .map(CustomFieldDTO::getId)
        		        .collect(Collectors.toSet());
            	List<PCProductCustomFieldTransaction> existingCustomFields = customFeildTransactionRepository
				        .findByProductIdAndIsActive(BigInteger.valueOf(productID), true);
            	for (PCProductCustomFieldTransaction existingField : existingCustomFields) {
				    if (!incomingCustomFieldIds.contains(existingField.getDestinationProductCustomFieldsId().intValue())) {
				        existingField.setActive(false);
				        existingField.setDeleted(true);
				        existingField.setStatus(PCConstants.UPDATE);
				        customFeildTransactionRepository.save(existingField);
				    }
            	}
                for (CustomFieldDTO customData : productCustomField) {
                    PCProductCustomFieldTransaction customField = new PCProductCustomFieldTransaction();
                    customField.setProductId(BigInteger.valueOf(productID));
                    customField.setDestinationProductId(BigInteger.valueOf(bcProductId));
                    customField.setDestinationProductCustomFieldsId(customData.getId());
                    customField.setName(customData.getName());
                    customField.setValue(customData.getValue());
                    customField.setActive(true);
                    customField.setDeleted(false);
                    Optional<PCProductCustomFieldTransaction> customFieldData = customFeildTransactionRepository.findByProductIdAndNameAndIsActive(BigInteger.valueOf(productID), customData.getName(), true);
                    if (customFieldData.isPresent()) {
                        customField.setStatus(PCConstants.UPDATE);
                        customField.setProductCustomFieldsId(customFieldData.get().getProductCustomFieldsId());
                        customFeildTransactionRepository.save(customField);
                    } else {
                        customField.setStatus(PCConstants.INSERT);
                        customFeildTransactionRepository.save(customField);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception occured when stored product custom field data in ADIS table from BC" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
        log.info("AdisLogService -> storeCustomFieldsDetails method ended");
        return PCConstants.SUCCESS;
    }
 

    public void CustomFeildToAdisLog(String Name, String Status, String Type, String Request, String Response,
                                     int customefeildId, int DestionationCustomefeildID) {
        try {
            log.info("AdisLogService.CustomFeildToAdislog method started");
            PCProductCustomFieldTransactionLog customFeildToAdis = new PCProductCustomFieldTransactionLog();
            customFeildToAdis.setName(Name);
            customFeildToAdis.setType(Type);
            customFeildToAdis.setStatus(Status);
            customFeildToAdis.setResponse(Response);
            customFeildToAdis.setRequest(Request);
            customFeildToAdis.setProductCustomFieldsId(customefeildId);
            customFeildToAdis.setDestinationProductCustomFieldsId(DestionationCustomefeildID);
            customFeildTransactionLogRepository.save(customFeildToAdis);
        } catch (Exception Ex) {
            log.error("Exception Occured in CustomFeildToAdisLog :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "CustomFeildToAdisLog", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    public void BrandToAdis(int BcBrandID, BrandDataDTO brandDataDTO, String status, PCBrandTransaction brand) {
        try {
            log.info("AdisLogService.BrandToAdis method started");
            if (status.equalsIgnoreCase(PCConstants.SUCCESS)) {
                brand.setCustomUrl(brandDataDTO.getCustom_url().getUrl());
                brand.setCustomized(brandDataDTO.getCustom_url().is_customized);
                brand.setDestinationBrandId(BcBrandID);
                brand.setStatus(status);
                brand.setActive(true);
                pcBrandTransactionRepository.save(brand);
            } else if (status.equalsIgnoreCase(PCConstants.DELETE)) {
                brand.setStatus(PCConstants.SUCCESS);
                brand.setActive(false);
                pcBrandTransactionRepository.save(brand);
            } else {
                brand.setActive(true);
                brand.setStatus(status);
                pcBrandTransactionRepository.save(brand);
            }
        } catch (Exception Ex) {
            log.error("Error occure while insert/updating brand data in ADIS table :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "BrandToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
        log.info("AdisLogService.BrandToAdis method ended");
    }

    public void BrandToAdisLog(int AdisBrandID, int BrandID, String Type, String Status, String Request, String
            Response) {
        try {
            log.info("AdisLogService.BrandToAdis method started");
            PCBrandTransactionLog brandTransactionLog = new PCBrandTransactionLog();
            brandTransactionLog.setBrandId(AdisBrandID);
            brandTransactionLog.setDestinationBrandId(BrandID);
            brandTransactionLog.setRequest(Request);
            brandTransactionLog.setResponse(Response);
            brandTransactionLog.setStatus(Status);
            brandTransactionLog.setType(Type);
            brandTransactionLogRepository.save(brandTransactionLog);
        } catch (Exception Ex) {
            log.error("Error occure while insert brand data in ADIS Log table :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "BrandToAdisLog", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

 /*   public void ProductBrandToAdis(int BcBrandID, PCProductBrandTransaction brandData) {
        try {
            PCProductBrandTransaction brands = new PCProductBrandTransaction();
            brands.setActive(brandData.isActive());
            brands.setBrandId(brandData.getBrandId());
            brands.setCustomUrl(brandData.getCustomUrl());
            brands.setDefaultProductSort(brandData.getDefaultProductSort());
            brands.setDescription(brandData.getDescription());
            brands.setBcBrandId(BcBrandID);
//            brands.setDestinationParentBrandId(brandData.get);
            brands.setImageUrl(brandData.getImageUrl());
            brands.setLayoutFile(brandData.getLayoutFile());
//            brands.setMetaDescription(brandData.getDescription());
//            brands.setMetaKeywords(brandData.getMetaKeywords());
            brands.setName(brandData.getName());
            brands.setPageTitle(brandData.getPageTitle());
            transactionRepository.save(brands);
        } catch (Exception Ex) {
            log.info("Error occure while insert brand data in ADIS Log table :" + ProductUtility.getStackTrace(Ex));
            emailService.sendErrorMail(secrets.getClientName(), "ProductBrandToAdis", ProductUtility.getStackTrace(Ex), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }

    } */

    public void storeCategoryLog(PCCategoryTransaction category, String response, String status, String type, String request) {
        try {
            PCCategoryTransactionLog logEntity = new PCCategoryTransactionLog();
            logEntity.setCategoryId(category.getCategoryId());
            logEntity.setRequest(request);
            logEntity.setDestinationCategoryId(category.getDestinationCategoryId());
            logEntity.setDestinationParentCategoryId(category.getDestinationParentCategoryId());
            logEntity.setParentCategoryId(category.getParentCategoryId());
            logEntity.setResponse(response);
            logEntity.setStatus(status);
            logEntity.setType(type);
            categoryLogRepo.save(logEntity);
        } catch (Exception e) {
            log.error("Error occur while insert category log into ADIS log table : " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "storeCategoryLog", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
        }
    }

    private Optional<ProductReportFieldsDTO> settingReportFields(List<PCProductTransactionLog> productLogList) {
        try {
            ProductReportFieldsDTO reportFields = new ProductReportFieldsDTO();

//            orderLogList.sort(Comparator.comparing(ProductTransactionLog::getSourceProductID));
            PCProductTransactionLog logEntity = productLogList.get(productLogList.isEmpty() ? 0 : (productLogList.size() - 1));
            log.info(INPROGRESS);
            Optional<PCProductTransaction> productDetails = PCProductTransactionRepository.findByStorehashAndDestinationProductIdAndIsActive(secrets.getStorehash(), logEntity.getDestinationProductId(), true);
            var productData = productDetails.get();

            reportFields.setSourceProductID(String.valueOf(productData.getSourceProductId()));
            reportFields.setDestinationProductID(String.valueOf(logEntity.getDestinationProductId()));
            if (productData.getSku() != null || !productData.getSku().isEmpty()) {
                reportFields.setProductSku(productData.getSku());
            }
            reportFields.setProductName(productData.getName());
            reportFields.setPrice(String.valueOf(productData.getPrice()));
            reportFields.setInventoryLevel(String.valueOf(productData.getInventoryLevel()));
            reportFields.setProductStatus(logEntity.getStatus());
            reportFields.setIntegrationType(logEntity.getType());
            reportFields.setInterfaceType("Product");

            return Optional.of(reportFields);
        } catch (Exception e) {
            log.error("Excpetion on productcommons.BCProductService.settingReportFields  " + ProductUtility.getStackTrace(e));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PCConstants.currentDateFormat);
            LocalDateTime now = LocalDateTime.now();
            String curDate = dtf.format(now);
//            emailService.sendErrorMail("settingReportFields", e.getMessage(), Constants.developerMailSub + secrets.getClientName() + curDate);
            emailService.sendErrorMail(secrets.getClientName(), "settingReportFields", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return Optional.empty();
        }
    }

    public String fetchProductReportData(String accessKey, String secretKey, String region, String activeProfile) {
        log.info("productcommons.BCProductvice.fetchOrderReportData method get started  ");
        try {
            String rawDate = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(rawDate);
            Calendar cal7 = Calendar.getInstance();
            cal7.setTime(date);
            cal7.add(Calendar.HOUR, -1);
            Date oneHourBack = cal7.getTime();
            Timestamp startDate = new java.sql.Timestamp(oneHourBack.getTime());
            log.info("startData  : " + startDate);
            List<PCProductTransactionLog> productLogDetails = PCProductTransactionLogRepository.getProductLogDetails(startDate);
            List<PCProductOptionSkuLogTransaction> productSkuLogDetails = productOptionSkuLog.getProductLogDetails(startDate);
            if (productLogDetails.isEmpty() && productSkuLogDetails.isEmpty()) {
                return "No product data found for the given time ";
            }
//            ProductReportFieldsDTO optionalReportFields = settingReportFields(productLogDetails);

//            Map<Integer, List<ProductTransactionLog>> productGroupByIdMap = productLogDetails.stream().collect(Collectors.groupingBy(ProductTransactionLog::getDestinationProductId));
//
//            List<ProductReportFieldsDTO> productReportFieldsList = new ArrayList<>();
//            for (Map.Entry<Integer, List<ProductTransactionLog>> entry : productGroupByIdMap.entrySet()) {
//                Optional<ProductReportFieldsDTO> optionalReportFields = settingReportFields(entry.getValue());
//                if (optionalReportFields.isPresent()) {
//                    productReportFieldsList.add(optionalReportFields.get());
//                }
//            }
            return writeReportFile(productLogDetails, productSkuLogDetails, accessKey, secretKey, region, activeProfile);
        } catch (ParseException e) {
            log.error("Exception on productcommons.BCproductService.fetchProductReportData  " + ProductUtility.getStackTrace(e));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PCConstants.currentDateFormat);
            LocalDateTime now = LocalDateTime.now();
            String curDate = dtf.format(now);
//            emailService.sendErrorMail("fetchOrderReportData", e.getMessage(), Constants.developerMailSub + secrets.getClientName() + curDate);
            emailService.sendErrorMail(secrets.getClientName(), "fetchProductReportData", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB + " " + curDate, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    private String writeReportFile
            (List<PCProductTransactionLog> productLogDetails, List<PCProductOptionSkuLogTransaction> productSkuLogDetails, String
                    accessKey, String secretKey, String region, String activeProfile) {
        try {
            File reportFileObj = File.createTempFile("aws-java-sdk-", ".xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet();
            String[] productHeaders = PCConstants.secrets.getProductHeader().split(",");
            Row headerRow = spreadsheet.createRow(0);
            for (int col = 0; col < productHeaders.length; col++) {
                headerRow.createCell(col).setCellValue(productHeaders[col]);
            }
            log.info("list of excel data for product : " + productLogDetails.size() + " variants : " + productSkuLogDetails.size());
            int rowIndex = 1;
            int successCount = 0;
            int failedCount = 0;
            if (productLogDetails.size() > 0) {
                for (PCProductTransactionLog data : productLogDetails) {
                    Optional<PCProductTransaction> productDetails = PCProductTransactionRepository.findByProductId(data.getProductId());
                    if (productDetails.isPresent()) {
                        var productData = productDetails.get();
                        Row row = spreadsheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(rowIndex - 1);
                        row.createCell(1).setCellValue(productData.getSourceProductId());
                        if (productData.getDestinationProductId() != null) {
                            row.createCell(2).setCellValue(productData.getDestinationProductId());
                        }
                        row.createCell(3).setCellValue(productData.getSku());
                        row.createCell(4).setCellValue(productData.getName());
                        if (productData.getPrice() != null) {
                            row.createCell(5).setCellValue(productData.getPrice().intValue());
                        }
                        if (productData.getInventoryLevel() != null) {
                            row.createCell(6).setCellValue(productData.getInventoryLevel());
                        }
                        row.createCell(7).setCellValue(data.getStatus());
                        if (data.getStatus().equalsIgnoreCase(PCConstants.SUCCESS)) {
                            row.createCell(8).setCellValue("Successfully integrated");
                            successCount++;
                        } else {
                            if (data.getResponse().startsWith("{")) {
                                JSONObject responseJSON = new JSONObject(data.getResponse());
                                row.createCell(8).setCellValue(responseJSON.getString("title"));
                            } else {
                                row.createCell(8).setCellValue("Failed to Integrate");
                            }
                            failedCount++;
                        }
//                        if (data.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
//                            successCount++;
//                        } else {
//                            failedCount++;
//                        }
                    } else {
                        log.info("Destination ID is not present in product");
                    }
                }
            }

            if (productSkuLogDetails.size() > 0) {
                for (PCProductOptionSkuLogTransaction optionSku : productSkuLogDetails) {
                    Optional<PCProductOptionSkuTransaction> variantDetails = productOptionSkuRepo.findById(optionSku.getProductOptionSkuID());
                    var variant = variantDetails.get();
                    Optional<PCProductTransaction> productDetails = PCProductTransactionRepository.findById(variant.getProductID());
                    var productData = productDetails.get();
                    Row row = spreadsheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rowIndex - 1);
                    row.createCell(1).setCellValue(productData.getSourceProductId());
                    if (productData.getDestinationProductId() != null) {
                        row.createCell(2).setCellValue(productData.getDestinationProductId());
                    }
                    row.createCell(3).setCellValue(variant.getSku());
                    row.createCell(4).setCellValue(productData.getName());
                    if (variant.getPrice() != null) {
                        row.createCell(5).setCellValue(variant.getPrice().intValue());
                    }
                    if (variant.getInventoryLevel() != null) {
                        row.createCell(6).setCellValue(variant.getInventoryLevel().intValue());
                    }
                    row.createCell(7).setCellValue(optionSku.getStatus());
                    if (optionSku.getStatus().equalsIgnoreCase(PCConstants.SUCCESS)) {
                        successCount++;
                    } else {
                        failedCount++;
                    }
                }
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PCConstants.currentDateFormat);
            LocalDateTime now = LocalDateTime.now();
            String curDate = dtf.format(now);

            String fileName = PCConstants.secrets.getClientName() + PCConstants.productReportFileName + curDate + PCConstants.excelExtention;

            try (FileOutputStream fileOutputStream = new FileOutputStream(reportFileObj)) {
                workbook.write(fileOutputStream);
            }

            s3Service.saveToS3Bucket(activeProfile, region, accessKey, secretKey, reportFileObj, fileName);
            emailService.sendReportMail(secrets.getClientName(), "Product Integration Report ", Files.readAllBytes(Paths.get(reportFileObj.getPath())), successCount, failedCount, config.getPrefix());
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on productcommons.BCProductService.writeReportFile  " + ProductUtility.getStackTrace(e));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PCConstants.currentDateFormat);
            LocalDateTime now = LocalDateTime.now();
            String curDate = dtf.format(now);
            emailService.sendErrorMail(secrets.getClientName(), "writeReportFile", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String storeProductOptionSkuData(int productId, ProductDTO productdata) {
        try {
            log.info("AdisLogService -> storeProductOptionSkuDetails method started");
            ArrayList<VariantDTO> variants = productdata.getVariants();
            for (VariantDTO variant : variants) {
                PCProductOptionSkuTransaction productOptionSku = new PCProductOptionSkuTransaction();
                productOptionSku.setProductID(productId);
                productOptionSku.setDestinationSkuID(variant.getSku_id());
                productOptionSku.setSku(variant.getSku());
                productOptionSku.setPrice(BigDecimal.valueOf(variant.getPrice()));
                productOptionSku.setCostPrice(variant.getCost_price() != 0 ? BigDecimal.valueOf(variant.getCost_price()) : BigDecimal.ZERO);
                productOptionSku.setSalePrice(variant.getSale_price() != 0 ? BigDecimal.valueOf(variant.getSale_price()) : BigDecimal.ZERO);
                productOptionSku.setRetailPrice(variant.getRetail_price() != 0 ? BigDecimal.valueOf(variant.getRetail_price()) : BigDecimal.ZERO);
                productOptionSku.setWeight(variant.getWeight() != 0 ? variant.getWeight() : 0.00);
                productOptionSku.setWidth(variant.getWidth() != 0 ? variant.getWidth() : 0.00);
                productOptionSku.setHeight(variant.getHeight() != 0 ? variant.getHeight() : 0.00);
                productOptionSku.setDepth(variant.getDepth() != 0 ? variant.getDepth() : 0.00);
                productOptionSku.setFixedCostShippingPrice(variant.getFixed_cost_shipping_price() != 0 ? BigDecimal.valueOf(variant.getDepth()) : BigDecimal.ZERO);
                productOptionSku.setUpc(variant.getUpc());
                productOptionSku.setInventoryLevel(BigDecimal.valueOf(variant.getInventory_level()));
                productOptionSku.setInventoryWarningLevel((int) variant.getInventory_warning_level());
                productOptionSku.setBinPickingNumber(variant.getBin_picking_number());
                productOptionSku.setFreeShipping(variant.is_free_shipping());
                productOptionSku.setPurchasingDisabled(variant.isPurchasing_disabled());
                productOptionSku.setActive(true);
                productOptionSku.setDeleted(false);
                productOptionSku.setImageUrl(variant.getImage_url() != null ? variant.getImage_url() : "");
                Optional<PCProductOptionSkuTransaction> productOptionSkus = productOptionSkuRepo.findByProductIDAndDestinationSkuIDAndIsActiveAndIsDeleted(productId, variant.getSku_id(), true, false);
                if (productOptionSkus.isPresent()) {
                    productOptionSku.setStatus(PCConstants.UPDATE);
                    productOptionSku.setProductOptionSkuID(productOptionSkus.get().getProductOptionSkuID());
                    productOptionSku = productOptionSkuRepo.save(productOptionSku);
                } else {
                    productOptionSku.setStatus(PCConstants.INSERT);
                    productOptionSku = productOptionSkuRepo.save(productOptionSku);
                }
                PCProductOptionSkuBackOrderDetailTransaction productoptionBackorderdetail = new PCProductOptionSkuBackOrderDetailTransaction();
                productoptionBackorderdetail.setProductOptionSkuId(productOptionSku.getProductOptionSkuID());
                productoptionBackorderdetail.setActive(true);
                productoptionBackorderdetail.setDeleted(false);
                Optional<PCProductOptionSkuBackOrderDetailTransaction> productoptionBackorder = productOptionSkuBackOrderDetailRepo.findByProductOptionSkuId(productOptionSku.getProductOptionSkuID());
                if (productoptionBackorder.isPresent()) {
                    productoptionBackorderdetail.setPromisingDate(productoptionBackorder.get().getPromisingDate() != null ? productoptionBackorder.get().getPromisingDate() : null);
                    productoptionBackorderdetail.setIsBackOrder(productoptionBackorder.get().getIsBackOrder() != null ? productoptionBackorder.get().getIsBackOrder() : null);
                    productoptionBackorderdetail.setItemSiteId(productoptionBackorder.get().getItemSiteId() != null ? productoptionBackorder.get().getItemSiteId() : null);
                    productoptionBackorderdetail.setIntegrationType(PCConstants.UPDATE);
                    productoptionBackorderdetail.setProductOptionSkuBackOrderDetailId(productoptionBackorder.get().getProductOptionSkuBackOrderDetailId());
                    productOptionSkuBackOrderDetailRepo.save(productoptionBackorderdetail);
                } else {
                    productoptionBackorderdetail.setIntegrationType(PCConstants.INSERT);
                    productOptionSkuBackOrderDetailRepo.save(productoptionBackorderdetail);
                }

                //storeProductImageDetails(productId, productOptionSku.getProductOptionSkuID(), productdata.getImages());
            }
        } catch (Exception e) {
            log.error("Exception occured when stored product variant data in ADIS table from BC" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
        log.info("AdisLogService -> storeProductOptionSkuDetails method ended");
        return PCConstants.SUCCESS;
    }

    public String storeProductOptionSkuLog(int productOptionSkuID, int destinationSkuID, String status, String
            request, String response, String type) {
        try {
            PCProductOptionSkuLogTransaction optionSkuLogEntity = new PCProductOptionSkuLogTransaction();
            optionSkuLogEntity.setProductOptionSkuID(productOptionSkuID);
            optionSkuLogEntity.setDestinationSkuID(destinationSkuID);
            optionSkuLogEntity.setStatus(status);
            optionSkuLogEntity.setRequest(request);
            optionSkuLogEntity.setResponse(response);
            optionSkuLogEntity.setType(type);
            optionSkuLogRepo.save(optionSkuLogEntity);
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on AdisLogService.storeProductOptionSkuLog  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "storeProductOptionSkuLog", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

//    public String storeProductImageDetails(int productId, int productOptionSkuId, ArrayList<
//            ImageDTO> imageDetails) {
//        try {
//            log.info("AdisLogService -> storeProductImageDetails method started");
//            PCProductImageTransaction productImageData = new PCProductImageTransaction();
//            productImageData.setProductId(BigInteger.valueOf(productId));
//            productImageData.setProductOptionSkuId(BigInteger.valueOf(productOptionSkuId));
//            productImageData.setActive(true);
//            productImageData.setDeleted(false);
//            //we need to set image URL
//            Optional<PCProductImageTransaction> productImage = productImageRepo.findByProductIdAndProductOptionSkuIdAndIsActiveAndIsDeleted(BigInteger.valueOf(productId), BigInteger.valueOf(productOptionSkuId), true, false);
//            if (productImage.isPresent()) {
//                productImageData.setProductImageId(productImage.get().getProductImageId());
//                productImageRepo.save(productImageData);
//            } else {
//                productImageRepo.save(productImageData);
//            }
//        } catch (Exception e) {
//            log.error("Exception occured when stored product data in ADIS table from BC" + ProductUtility.getStackTrace(e));
//            return PCConstants.FAILED;
//        }
//        log.info("AdisLogService -> storeProductImageDetails method ended");
//        return PCConstants.SUCCESS;
//    }
    
    public void storeProductImageDetails(int productId, ArrayList<ImageDTO> imageDetails) {
		Set<Integer> incomingImageIds = imageDetails.stream()
		        .map(ImageDTO::getId)
		        .collect(Collectors.toSet());
 
		    // Fetch all active images for the product
		    List<PCProductImageTransaction> existingImages = productImageRepo
		        .findByProductIdAndIsActiveAndIsDeleted(BigInteger.valueOf(productId), true , false);
 
		    // Deactivate images not present in imageDetails
		    for (PCProductImageTransaction existingImage : existingImages) {
		        if (!incomingImageIds.contains(existingImage.getDestinationProductImageId())) {
		            existingImage.setActive(false);
		            existingImage.setDeleted(true);
		            existingImage.setStatus(PCConstants.UPDATE);
		            productImageRepo.save(existingImage);
		        }
		    }
		for (ImageDTO image : imageDetails) {
			PCProductImageTransaction productImageData = new PCProductImageTransaction();
			productImageData.setProductId(BigInteger.valueOf(productId));
			productImageData.setActive(true);
			productImageData.setDeleted(false);
			productImageData.setDestinationProductImageId(image.getId());
			productImageData.setImageUrl(image.getImage_url());
			productImageData.setImageFile(image.getImage_file());
			productImageData.setUrlStandard(image.getUrl_standard());
			productImageData.setUrlZoom(image.getUrl_zoom());
			productImageData.setUrlTiny(image.getUrl_tiny());
			Optional<PCProductImageTransaction> productImage = productImageRepo.findByProductIdAndDestinationProductImageIdAndIsActiveAndIsDeleted(BigInteger.valueOf(productId), image.getId(), true, false);
			if (productImage.isPresent()) {
				productImageData.setProductImageId(productImage.get().getProductImageId());
				productImageData.setStatus(PCConstants.UPDATE);
			} else {
				productImageData.setStatus(PCConstants.INSERT);
			}
			productImageRepo.save(productImageData);
		}
	}
 
    

    public String storeProductOptionData(int productid, ProductDTO productData) {
        try {
            log.info("AdisLogService -> storeProductOptionDetails method started");
            List<OptionData> optionDetails = productData.getOptions();
            log.info("Product option stored in ADIS table from BC." + optionDetails);
            for (OptionData option : optionDetails) {
                //option_id v2 version or v3 version. //v3 version we used
                PCProductOptionTransaction productOption = new PCProductOptionTransaction();
                productOption.setProductID(BigInteger.valueOf(productid));
                productOption.setDestinationOptionID(BigInteger.valueOf(option.getId()));//option id set in v2 version. but we used v3 version.
                productOption.setDisplayName(option.getDisplay_name());
                productOption.setType(option.getType());
                productOption.setSortOrder(option.getSort_order());
                productOption.setDestinationOptionAssignID(option.getId());
                productOption.setActive(true);
                productOption.setDeleted(false);
                Optional<PCProductOptionTransaction> productOptionData = productOptionRepo.findByProductIDAndDestinationOptionAssignIDAndIsActive(BigInteger.valueOf(productid), option.getId(), true);
                if (productOptionData.isPresent()) {
                    productOption.setStatus(PCConstants.UPDATE);
                    productOption.setProductOptionsID(productOptionData.get().getProductOptionsID());
                    productOption = productOptionRepo.save(productOption);
                } else {
                    productOption.setStatus(PCConstants.INSERT);
                    productOption = productOptionRepo.save(productOption);
                }
                ArrayList<OptionValue> productOptionValue = option.getOption_values();
                log.info("Product option value stored in ADIS table from BC." + productOptionValue);
                for (OptionValue optionValue : productOptionValue) {
                    PCProductOptionValueTransaction optionvalueData = new PCProductOptionValueTransaction();
                    optionvalueData.setProductID(BigInteger.valueOf(productid));
                    optionvalueData.setDestinationOptionValueID(BigInteger.valueOf(optionValue.getId()));
                    optionvalueData.setProductOptionsID(BigInteger.valueOf(productOption.getProductOptionsID()));
                    optionvalueData.setDefault(optionValue.is_default());
                    optionvalueData.setLabel(optionValue.getLabel());
                    optionvalueData.setValueData(optionValue.getLabel());
                    optionvalueData.setSortOrder(optionValue.getSort_order());
                    optionvalueData.setActive(true);
                    optionvalueData.setDeleted(false);
                    Optional<PCProductOptionValueTransaction> productOptionValueData = productOptionValueRepo.findByProductIDAndDestinationOptionValueIDAndProductOptionsIDAndIsActive(productid, BigInteger.valueOf(optionValue.getId()), BigInteger.valueOf(productOption.getProductOptionsID()), true);
                    if (productOptionValueData.isPresent()) {
                        optionvalueData.setStatus(PCConstants.UPDATE);
                        optionvalueData.setProductOptionValueID(productOptionValueData.get().getProductOptionValueID());
                        optionvalueData = productOptionValueRepo.save(optionvalueData);
                    } else {
                        optionvalueData.setStatus(PCConstants.INSERT);
                        optionvalueData = productOptionValueRepo.save(optionvalueData);
                    }
                    //product.tbl_product_option_sku_details we need to find solution.
                    ArrayList<VariantDTO> variantdatas = productData.getVariants();
                    for (VariantDTO variantdata : variantdatas) {
                        List<ProductOptionValueDTO> optionskusVariant = variantdata.getOption_values().stream()
                                .filter(variant -> variant.getId() == optionValue.getId())
                                .collect(Collectors.toList());
                        if (optionskusVariant.size() == 1) {
                            Optional<PCProductOptionSkuTransaction> productOptionSkus = productOptionSkuRepo.findByProductIDAndDestinationSkuIDAndIsActiveAndIsDeleted(productid, variantdata.getSku_id(), true, false);
                            //ithoda pk, option and option value pk insert in ADIS table.
                            storeProductOptionSkuDetails(productid, productOptionSkus.get().getProductOptionSkuID(), productOption.getProductOptionsID(), optionvalueData.getProductOptionValueID(), optionskusVariant);
                            break;
                        } else {
                            log.info("option value id and variant response not matched" + optionskusVariant);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception occured when stored product option data in ADIS table from BC" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
        log.info("AdisLogService -> storeProductOptionDetails method ended");
        return PCConstants.SUCCESS;
    }

    public String storeProductOptionSkuDetails(int productID, int productOptionSkuID, int productOptionId,
                                               int productOptionValueId, List<ProductOptionValueDTO> optionskusVariant) {
        try {
            log.info("AdisLogService -> storeProductOptionSkuDetails method started");
            PCProductOptionSkuDetailsTransaction productOptionSkuDetails = new PCProductOptionSkuDetailsTransaction();
            productOptionSkuDetails.setOptionID(BigInteger.valueOf(productOptionId));
            productOptionSkuDetails.setOptionName(optionskusVariant.get(0).getOption_display_name());
            productOptionSkuDetails.setOptionValueID(BigInteger.valueOf(productOptionValueId));
            productOptionSkuDetails.setOptionValue(optionskusVariant.get(0).getLabel());
            productOptionSkuDetails.setProductID(BigInteger.valueOf(productID));
            productOptionSkuDetails.setProductOptionSkuID(BigInteger.valueOf(productOptionSkuID));
            productOptionSkuDetails.setActive(true);
            productOptionSkuDetails.setDeleted(false);
            //option value id same and is active and is deleted.
            Optional<PCProductOptionSkuDetailsTransaction> optionSkuDetails = productOptionSkuDetailsRepo.findByOptionValueIDAndIsActive(BigInteger.valueOf(productOptionValueId), true);
            if (optionSkuDetails.isPresent()) {
                productOptionSkuDetails.setStatus(PCConstants.UPDATE);
                productOptionSkuDetails.setProductOptionSkuDetailsID(optionSkuDetails.get().getProductOptionSkuDetailsID());
                productOptionSkuDetailsRepo.save(productOptionSkuDetails);
            } else {
                productOptionSkuDetails.setStatus(PCConstants.INSERT);
                productOptionSkuDetailsRepo.save(productOptionSkuDetails);
            }
        } catch (Exception e) {
            log.error("Exception occured when stored product option sku details data in ADIS table from BC" + ProductUtility.getStackTrace(e));
            return PCConstants.FAILED;
        }
        log.info("AdisLogService -> storeProductOptionSkuDetails method ended");
        return PCConstants.SUCCESS;
    }

    public String storeProductOptionLog(int productOptionsID, int destinationOptionID, String status, String
            request, String response, String type) {
        try {
            PCProductOptionLogTransaction optionLogEntity = new PCProductOptionLogTransaction();
            optionLogEntity.setDestinationOptionID(destinationOptionID);
            optionLogEntity.setProductOptionsID(productOptionsID);
            optionLogEntity.setStatus(status);
            optionLogEntity.setRequest(request);
            optionLogEntity.setResponse(response);
            optionLogEntity.setType(type);
            optionLogRepo.save(optionLogEntity);
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on AdisLogService.storeProductOptionLog  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "storeProductOptionLog", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }

    public String storeProductOptionValueLog(int productOptionValueID, int destinationOptionValueID, String
            status, String request, String response, String type) {
        try {
            PCProductOptionValueLogTransaction optionValueLogEntity = new PCProductOptionValueLogTransaction();
            optionValueLogEntity.setProductOptionValueID(productOptionValueID);
            optionValueLogEntity.setDestinationOptionValueID(destinationOptionValueID);
            optionValueLogEntity.setStatus(status);
            optionValueLogEntity.setRequest(request);
            optionValueLogEntity.setResponse(response);
            optionValueLogEntity.setType(type);
            optionValueLogRepo.save(optionValueLogEntity);
            return PCConstants.SUCCESS;
        } catch (Exception e) {
            log.error("Exception on AdisLogService.storeProductOptionValueLog  " + ProductUtility.getStackTrace(e));
            emailService.sendErrorMail(secrets.getClientName(), "storeProductOptionValueLog", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, config.getPrefix());
            return PCConstants.FAILED;
        }
    }
}
