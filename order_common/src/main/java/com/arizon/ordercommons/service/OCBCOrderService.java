/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.service;

import static com.arizon.ordercommons.configs.OCConstants.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.arizon.ordercommons.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arizon.ordercommons.configs.OCAwsSecrets;
import com.arizon.ordercommons.configs.OCConstants;
import com.arizon.ordercommons.entity.OCBCCustomerTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderProductTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderTransactionLog;
import com.arizon.ordercommons.entity.OCBCOrderWebhookTableTransaction;
import com.arizon.ordercommons.entity.OCBCProductTableTransaction;
import com.arizon.ordercommons.entity.OCBCShippingAddressTransaction;
import com.arizon.ordercommons.entity.OCBCShippingMethodsTransaction;
import com.arizon.ordercommons.repository.OCBCCustomerTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderProductTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderShippingAddressTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionLogRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderWebhookTransactionRepository;
import com.arizon.ordercommons.repository.OCBCProductTransactionRepository;
import com.arizon.ordercommons.repository.OCBCShippingMethodTransactionRepository;
import com.arizon.ordercommons.restclient.OCBigcommerceClient;
import com.arizon.ordercommons.restclient.OCRestClientBuilder;
import com.arizon.ordercommons.util.OCAWSBucketService;
import com.arizon.ordercommons.util.OrderUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kalaivani.r
 */
@Service
@Slf4j
public class OCBCOrderService {

    @Autowired
    OCRestClientBuilder clientBuilder;

    @Autowired
    OCBCOrderTransactionRepository orderRepo;

    @Autowired
    OCBCOrderProductTransactionRepository orderProductRepo;

    @Autowired
    private OCOrderLogServices orderLogServices;

    @Autowired
    private OCBCCustomerTransactionRepository customerRepo;

    @Autowired
    private OCBCProductTransactionRepository productRepo;

    @Autowired
    OCBCOrderWebhookTransactionRepository webRepo;

    @Autowired
    OCBCShippingMethodTransactionRepository shippingMethodRepo;

    @Autowired
	OCBCOrderShippingAddressTransactionRepository shippingAddressRepo;

	@Autowired
    OCBCOrderWebhookTransactionRepository bcOrderWebhookTransactionRepository;

    @Autowired
    private OCBCOrderTransactionLogRepository orderLogRepo;

    @Autowired
    private OCAWSBucketService s3Service;

    @Autowired
    private OCEmailServices emailService;

    @Autowired
    OCAwsSecrets secrets;

    public OrderWebhookStatusDTO fetchWebHookPendingOrders(String webhookStatus, String process) {
        OrderWebhookStatusDTO status = new OrderWebhookStatusDTO();
        try {
            log.info("Entered into ordercommons.BCOrderService.fetchWehBookPendingOrders method storehash:" + secrets.getStorehash());
            List<OCBCOrderWebhookTableTransaction> webHookOrderList;
            if (process.equalsIgnoreCase(OCConstants.NORMAL_PROCESS)) {
                webHookOrderList = webRepo.findByStatusAndStorehash(webhookStatus, secrets.getStorehash());
            } else {
                webHookOrderList = webRepo.getFailedWebhookDetails(webhookStatus, secrets.getStorehash());
            }

            log.info("Total pending order found : " + webHookOrderList.size());

            for (OCBCOrderWebhookTableTransaction webHookOrder : webHookOrderList) {
//                webHookOrder.setRetryCount(webHookOrder.getRetryCount() + 1);
                webHookOrder.setStatus(OCConstants.WebhookInProgressStatus);
                webRepo.save(webHookOrder);
                status = getBCOrderData(webHookOrder.getDestinationOrderId());
                log.info("BC to ADIS flow --> order : " + webHookOrder.getDestinationOrderId() + "status  -->  " + status);
                if (!OCConstants.success.equalsIgnoreCase(status.getStatus())) {
                    if (webHookOrder.getRetryCount() == secrects.getRetryCount()) {
                        log.info("Changing the status of the order to 'Manual Verification Required for order : " + webHookOrder.getDestinationOrderId());
                        //Ram Temp comment                changeOrderStatusBC(12, webHookOrder.getDestinationOrderId());
                        webHookOrder.setRetryCount(webHookOrder.getRetryCount() + 1);
                    } else {
                        webHookOrder.setRetryCount(webHookOrder.getRetryCount() + 1);
                    }
                }
                webHookOrder.setStatus(status.getStatus());
                webRepo.save(webHookOrder);
            }
            return status;
        } catch (Exception e) {
            log.error("Exception on ordercommons.BCOrderService.fetchWehBookPendingOrders  " + OrderUtility.getStackTrace(e));
            return status;
        }
    }

    public OrderWebhookStatusDTO getBCOrderData(int orderID) {
        log.info("BCOrderservice class -> getBCOrderData method started");

        OrderWebhookStatusDTO status = new OrderWebhookStatusDTO();
        try {
            ObjectMapper mapper = new ObjectMapper();

            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            log.info("Get order data from BC method start");
            feign.Response bcOrderResponse = bcRestClientBuilder.getOrderById(orderID, secrects.getAccessToken(), secrects.getStorehash());
            String bcOrderReponseBody = IOUtils.toString(bcOrderResponse.body().asReader());
            log.info("order response" + bcOrderReponseBody);
            
            feign.Response bcOrderLineItemResponse = bcRestClientBuilder.getOrderProductDetails(orderID, secrects.getAccessToken(), secrects.getStorehash());
            String bcOrderProductResponseBody = IOUtils.toString(bcOrderLineItemResponse.body().asReader());

            log.info("order products response" + bcOrderProductResponseBody);
            feign.Response bcShippingAddresssResponse = bcRestClientBuilder.getOrderShippingAddress(orderID, secrects.getAccessToken(), secrects.getStorehash());
            String bcShippingAddResBody = IOUtils.toString(bcShippingAddresssResponse.body().asReader());

            log.info("order shipment address" + bcShippingAddResBody);

            BCOrderShipmentsDTO bcShipments = bcRestClientBuilder.getOrderShipments(orderID, secrects.getAccessToken(), secrects.getStorehash());

            log.info("order shipments" + bcShipments);


            if (bcOrderResponse.status() == 200 && bcOrderLineItemResponse.status() == 200 && bcShippingAddresssResponse.status() == 200) {
                BCOrderResponseDTO bcResponse = mapper.readValue(bcOrderReponseBody, new TypeReference<BCOrderResponseDTO>() {

                });
                List<BCOrderProductResponseDTO> bcProductResponse = mapper.readValue(bcOrderProductResponseBody, new TypeReference<List<BCOrderProductResponseDTO>>() {

                });
                log.info("bcProductResponse" + bcProductResponse);
                List<BCOrderShippingResponseDTO> bcShippingAddressResponse = mapper.readValue(bcShippingAddResBody, new TypeReference<List<BCOrderShippingResponseDTO>>() {

                });

                Optional<OCBCOrderTableTransaction> optionalOrderData = storeOrderDetailsADIS(bcResponse, bcShippingAddressResponse);
                status.setPaymentProviderId(bcResponse.getPayment_provider_id());
                if (optionalOrderData.isPresent()) {
                    OCBCOrderTableTransaction adisOrderData = optionalOrderData.get();
                    checkProductLineItems(adisOrderData.getOrderId(), bcProductResponse);
                    status.setStatus(storeOrderProductDetailsADIS(bcProductResponse, bcShippingAddressResponse, optionalOrderData.get(), bcShipments, bcResponse));
                    if (status.getStatus().equalsIgnoreCase(OCConstants.success)) {
                        status.setStatus(storeShippingDetailsADIS(bcShippingAddressResponse, optionalOrderData.get()));
                        return status;
                    }
                } else {
                    return status;
                }
            } else {
                log.info("Couldn't connect resclient builder to Big commerce for fetch the orders" + bcOrderResponse);
                emailService.sendErrorMail("BCOrderServices.getBCOrderData", "Couldn't connect resclient builder to Big commerce for fetch the orders ", secrects.getClientName() + OCConstants.developerMailSub);
            }

        } catch (IOException e) {
            log.info("Exception found in order integrate from Bigcommerce to ADIS" + OrderUtility.getStackTrace(e));
            status.setStatus(OCConstants.failed);
        }
        log.info("BCOrderservice class -> getBCOrderData method ended");
        return status;
    }

    public Optional<OCBCOrderTableTransaction> storeOrderDetailsADIS(BCOrderResponseDTO bcOrderResponse, List<BCOrderShippingResponseDTO> bcShippingAddressResponse) throws IOException {
        log.info("BCOrderservice class -> storeOrderDetailsADIS method started");
        ObjectMapper mapper = new ObjectMapper();
        String processType = OCConstants.insert;
        boolean isStoreLog = true;
        try {
            OCBCOrderTableTransaction orderData;
            Optional<OCBCOrderTableTransaction> optionalOrder = orderRepo.findByStorehashAndSourceOrderId(secrects.getStorehash(), bcOrderResponse.getId());
            if (!optionalOrder.isPresent()) {
                orderData = new OCBCOrderTableTransaction();
                orderData.setDestinationOrderId("0");
                orderData.setOrderReferenceNumber(0);
            } else {
                orderData = optionalOrder.get();
                processType = OCConstants.update;
            }

            Optional<OCBCCustomerTableTransaction> optionalCustomer;
            if (bcOrderResponse.getCustomer_id() != 0) {
                optionalCustomer = customerRepo.findByStorehashAndDestinationCustomerID(secrects.getStorehash(), bcOrderResponse.getCustomer_id());
            } else {
                optionalCustomer = customerRepo.findByStorehashAndEmailAddressAndIsActive(secrects.getStorehash(), bcOrderResponse.getBilling_address().getEmail(), true);
            }

            if (optionalCustomer.isPresent()) {
                OCBCCustomerTableTransaction customerData = optionalCustomer.get();
                orderData.setSourceCustomerId(customerData.getDestinationCustomerID());
                orderData.setDestinationCustomerId(customerData.getSourceCustomerID());     // need to clarify with the team.
            } else {
                log.info("Customer ID not found in ADIS for BC Customer ID  :  " + bcOrderResponse.getCustomer_id());
//                emailService.sendErrorMail("BCOrderService.storeOrderDetailsADIS", "NS Customer ID not found in ADIS for BC Customer ID  :  " + bcOrderResponse.getCustomer_id(), secrects.getClientName() + Constants.developerMailSub);
//                emailService.sendAlertMail("Customer ID not found in our internal table for customer email :  " + bcOrderResponse.getBilling_address().getEmail() + " for order : " + bcOrderResponse.getId(), secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());

                orderData.setDestinationCustomerId(String.valueOf(secrects.getGuest_CustomerId()));      // need to set the customer id of the guest customer of the NS or GP
            }

            orderData.setSourceOrderId(bcOrderResponse.getId());
            orderData.setEmailAddress(bcOrderResponse.getBilling_address().getEmail());
            orderData.setOrderStatus(bcOrderResponse.getStatus());
            if (secrects.getOrderStatus().contains(orderData.getOrderStatus())) {
                orderData.setIntegrationStatus(OCConstants.PendingStatus);
            } else {
                orderData.setIntegrationStatus(OCConstants.HoldStatus);
                isStoreLog = false;
            }
            orderData.setCurrency(bcOrderResponse.getCurrency_code());
            orderData.setShippingAmount(new BigDecimal(bcOrderResponse.getShipping_cost_ex_tax()));
            orderData.setShippingAmountIncTax(new BigDecimal(bcOrderResponse.getShipping_cost_inc_tax()));
            orderData.setTaxAmount(new BigDecimal(bcOrderResponse.getTotal_tax()));
            orderData.setSubtotal(new BigDecimal(bcOrderResponse.getSubtotal_inc_tax()));
            orderData.setTotalIncTax(new BigDecimal(bcOrderResponse.getTotal_inc_tax()));
            orderData.setTotalExcTax(new BigDecimal(bcOrderResponse.getTotal_ex_tax()));
            orderData.setDiscountAmount(new BigDecimal(bcOrderResponse.getDiscount_amount()));
            orderData.setOrderNotes(bcOrderResponse.getCustomer_message());//dout order comment
            if (secrects.getClientName().equalsIgnoreCase(OCConstants.clientName)) {
                orderData.setPaymentMethod(bcOrderResponse.getPayment_method());
            } else {
                if (bcOrderResponse.getPayment_method().equalsIgnoreCase("Purchase Order Number")) {
                    orderData.setPaymentMethod(bcOrderResponse.getPayment_method());
                } else if (bcOrderResponse.getPayment_method().equalsIgnoreCase("For Credit Card provide Email and/or Contact Number")) {
                    orderData.setPaymentMethod(bcOrderResponse.getPayment_method());
                } else {
                    OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());

                    feign.Response bcOrderPaymentResponse = bcRestClientBuilder.getPaymentMethodName(secrects.getAccessToken(), secrects.getStorehash(), bcOrderResponse.getId());
                    if (bcOrderPaymentResponse.status() == 200) {
                        String bcOrderPaymentReponseBody = IOUtils.toString(bcOrderPaymentResponse.body().asReader());
                        log.info("bc order payament response: " + bcOrderPaymentReponseBody);
                        RootDTO bcPaymentResponse = mapper.readValue(bcOrderPaymentReponseBody, RootDTO.class);
                        orderData.setPaymentMethod(bcPaymentResponse.getData() != null && !bcPaymentResponse.getData().isEmpty() && bcPaymentResponse.getData().get(0).getCredit_card() != null ? bcPaymentResponse.getData().get(0).getCredit_card().getCard_type() : null);
                    } else {
                        log.info("Couldn't connect resclient builder to Big commerce for fetch the order payment details: " + bcOrderPaymentResponse);
                        emailService.sendErrorMail("BCOrderServices.storeOrderDetailsADIS", "Couldn't connect resclient builder to Big commerce for fetch the order payment details: ", secrects.getClientName() + OCConstants.developerMailSub);
                    }

                }
            }
            orderData.setRefundedAmount(new BigDecimal(bcOrderResponse.getRefunded_amount()));
            orderData.setCouponDiscountAmount(new BigDecimal(bcOrderResponse.getCoupon_discount()));
            orderData.setGiftCertificateAmount(new BigDecimal(bcOrderResponse.getGift_certificate_amount()));
            orderData.setStaffNotes(bcOrderResponse.getStaff_notes());
            orderData.setBillingFirstname(bcOrderResponse.getBilling_address().getFirst_name());
            orderData.setBillingLastname(bcOrderResponse.getBilling_address().getLast_name());
            orderData.setBillingAddress1(bcOrderResponse.getBilling_address().getStreet_1());
            orderData.setBillingAddress2(bcOrderResponse.getBilling_address().getStreet_2());
            orderData.setBillingCity(bcOrderResponse.getBilling_address().getCity());
            orderData.setBillingState(bcOrderResponse.getBilling_address().getState());
            orderData.setBillingZip(bcOrderResponse.getBilling_address().getZip());
            orderData.setBillingCountry(bcOrderResponse.getBilling_address().getCountry());
            orderData.setBillingCompany(bcOrderResponse.getBilling_address().getCompany());
            orderData.setBillingPhone(bcOrderResponse.getBilling_address().getPhone());
            orderData.setBillingEmail(bcOrderResponse.getBilling_address().getEmail());
            orderData.setBillingCountryIso2(bcOrderResponse.getBilling_address().getCountry_iso2());
            orderData.setOrderCogs(BigDecimal.ZERO);
            orderData.setOrderProfit(BigDecimal.ZERO);
            orderData.setSubtotalExcTax(new BigDecimal(bcOrderResponse.getSubtotal_ex_tax()));

            if (bcShippingAddressResponse != null && !bcShippingAddressResponse.isEmpty() && bcShippingAddressResponse.get(0).getForm_fields() != null &&
                    !bcShippingAddressResponse.get(0).getForm_fields().isEmpty()) {
                for (BCFormFieldsDTO bcFormFieldsDTO : bcShippingAddressResponse.get(0).getForm_fields()) {
                    if ("Source_Vendor".equalsIgnoreCase(bcFormFieldsDTO.getName())) {
                        orderData.setSourceVendor(bcFormFieldsDTO.getValue());
                    }
                }
            }

            orderData.setSourceCustomerId(bcOrderResponse.getCustomer_id());
            if (bcOrderResponse.getStatus().equalsIgnoreCase("Cancelled")) {
                orderData.setActive(false);
                orderData.setDeleted(true);
            } else {
                orderData.setActive(true);
                orderData.setDeleted(false);
            }
            orderData.setSingleShippingAddress(true);
            orderData.setCreatedBy(0);
            orderData.setModifiedBy(0);

            SimpleDateFormat dateformatter1 = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss +0000");
            Date date = dateformatter1.parse(bcOrderResponse.getDate_created());

            SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String orderCreatedate = dateformatter.format(date);

            date = dateformatter1.parse(bcOrderResponse.getDate_created());
            String orderModifieddate = dateformatter.format(date);

            orderData.setOrderCreatedDate(orderCreatedate);
            orderData.setOrderModifiedDate(orderModifieddate);
            orderData.setOrderRefundedDate(0);//if we need, to set
            orderData.setOrderPaidDate(0);//if we need, to set
            orderData.setOrderCancelledDate(0);//if we need, to set
            orderData.setMerchantComment("");//if we need, to set
            orderData.setOrder_is_deleted(false);
            orderData.setStoreCredit(new BigDecimal(bcOrderResponse.getStore_credit_amount()));
            orderData.setRetryCount(0);
            orderData.setStorehash(secrects.getStorehash());
            orderData = orderRepo.save(orderData);
            if (isStoreLog) {
                orderLogServices.storeOrderLogDetails(orderData.getOrderId(), orderData.getDestinationOrderId(), OCConstants.success, "", OCConstants.successReponse, processType, orderData.getSourceOrderId(), OCConstants.bcInterface);
            }

            return Optional.of(orderData);
        } catch (Exception e) {//need to change exception
            log.info("exception found in insert order data in ADIS table from BigCommerce " + OrderUtility.getStackTrace(e));
            orderLogServices.storeOrderLogDetails(0, "0", OCConstants.failed, "", e.getLocalizedMessage(), processType, bcOrderResponse.getId(), OCConstants.bcInterface);
            emailService.sendErrorMail("storeOrderDetailsADIS", OrderUtility.getStackTrace(e), secrects.getClientName() + OCConstants.developerMailSub);
            return Optional.empty();
        }
    }

    private String checkProductLineItems(int orderID, List<BCOrderProductResponseDTO> bcProductResponseList) {
        log.info("Entered into ordercommons.BCOrderService.checkProductLineItems method");
        try {

            List<OCBCOrderProductTableTransaction> orderedProductList = orderProductRepo.findByorderId(orderID);
            if (orderedProductList.isEmpty()) {
                log.info("Exited from ordercommons.BCOrderService.checkProductLineItems method");
                return OCConstants.success;
            }
            List<Integer> adisOrderedProductList = orderedProductList.stream().map(OCBCOrderProductTableTransaction::getSourceOrderLineitemId).collect(Collectors.toList());
            log.info("adisOrderedProductList  :  " + adisOrderedProductList);
            List<Integer> bcOrderedProductList = bcProductResponseList.stream().map(BCOrderProductResponseDTO::getId).collect(Collectors.toList());
            log.info("bcOrderedProductList  : " + bcOrderedProductList);
            adisOrderedProductList.removeAll(bcOrderedProductList);
            log.info("cancelled order lineitem id's  :  " + adisOrderedProductList);
            for (Integer orderLineItemID : adisOrderedProductList) {
                Optional<OCBCOrderProductTableTransaction> optionalOrderedProduct = orderProductRepo.findBysourceOrderLineitemId(orderLineItemID);
                OCBCOrderProductTableTransaction orderedProduct = optionalOrderedProduct.get();
                orderedProduct.setActive(false);
                orderedProduct.setDeleted(true);
                orderProductRepo.save(orderedProduct);
            }
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on ordercommons.BCOrderService.checkProductLineItems  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.checkProductLineItems", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    private String storeOrderProductDetailsADIS(List<BCOrderProductResponseDTO> bcProductResponseList, List<BCOrderShippingResponseDTO> bcShippingAddressResponse, OCBCOrderTableTransaction orderData, BCOrderShipmentsDTO bcShipment, BCOrderResponseDTO bcOrderResponse) {
        try {
            log.info("BCOrderservice class -> storeOrderProductDetailsADIS method started");
            String processType;

            for (BCOrderProductResponseDTO orderedProduct : bcProductResponseList) {

                OCBCOrderProductTableTransaction orderProductData;
                Optional<OCBCOrderProductTableTransaction> optionalOrderProduct = orderProductRepo.findBysourceOrderLineitemId(orderedProduct.getId());

                if (!optionalOrderProduct.isPresent()) {
                    orderProductData = new OCBCOrderProductTableTransaction();
                    processType = OCConstants.insert;
                    orderProductData.setDestinationOrderLineitemId(0);
                } else {
                    orderProductData = optionalOrderProduct.get();
                    processType = OCConstants.update;
                }

                orderProductData.setOrderId(orderData.getOrderId());
                orderProductData.setSourceProductId(orderedProduct.getProduct_id());
                orderProductData.setProductName(orderedProduct.getName());
                orderProductData.setQuantity(orderedProduct.getQuantity());
                orderProductData.setSku(orderedProduct.getSku());
                orderProductData.setLineitemPrice(new BigDecimal(orderedProduct.getBase_price()));
                orderProductData.setLineitemTotalCostPrice(new BigDecimal(orderedProduct.getTotal_inc_tax()));
                orderProductData.setLineitemSalePrice(new BigDecimal(orderedProduct.getBase_price()));
                orderProductData.setLineitemTax(new BigDecimal(orderedProduct.getTotal_tax()));
                orderProductData.setLineitemCostPrice(new BigDecimal(orderedProduct.getBase_cost_price()));
                orderProductData.setLineitemPriceIncTax(new BigDecimal(orderedProduct.getPrice_inc_tax()));

                orderProductData.setSourceOrderLineitemId(orderedProduct.getId());
                if(orderedProduct.getApplied_discounts() != null && !orderedProduct.getApplied_discounts().isEmpty()) {
                	log.info("Found a Valid Coupon for the line item");
                	orderProductData.setCouponDiscountAmount(new BigDecimal(orderedProduct.getApplied_discounts().get(0).getAmount()));
                } else {
                	log.info("No coupon found setting coupon setting Coupon Discount Amount as "+BigDecimal.ZERO.setScale(2));
                	orderProductData.setCouponDiscountAmount(BigDecimal.ZERO.setScale(2));
                }
                if (bcShippingAddressResponse != null && !bcShippingAddressResponse.isEmpty()) {
                    orderProductData.setShippingAmount(new BigDecimal(bcShippingAddressResponse.get(0).getBase_cost()));
                    orderProductData.setShippingAmountIncTax(new BigDecimal(bcShippingAddressResponse.get(0).getCost_inc_tax()));
                    orderProductData.setShippingMethod(bcShippingAddressResponse.get(0).getShipping_method());
                    Optional<OCBCShippingMethodsTransaction> shippingMethodID = shippingMethodRepo.findByshippingMethodName(bcShippingAddressResponse.get(0).getShipping_method());
                    if (shippingMethodID.isPresent()) {
                        orderProductData.setShippingMethodId(shippingMethodID.get().getShippingMethodID());
                    } else {
                        orderProductData.setShippingMethodId(0);
                    }
                }
                log.info("gift certificate " + bcOrderResponse.getGift_certificate_amount());
                if (bcOrderResponse.getGift_certificate_amount().equalsIgnoreCase("0.0000")) {//need to check
                    orderProductData.setGiftCertificate(false);
                } else {
                    orderProductData.setGiftCertificate(true);
                }
                orderProductData.setActive(true);
                orderProductData.setDeleted(false);
                orderProductData.setCreatedBy(0);
                orderProductData.setModifiedBy(0);
                orderProductData.setShipment("");
                if (null != bcShipment) {
                    orderProductData.setTracking(bcShipment.getTracking_number());
                }
                orderProductData.setBrandName(orderedProduct.getBrand());

                Optional<OCBCProductTableTransaction> optionalProduct = productRepo.findFirstByStorehashAndSku(secrects.getStorehash(), orderedProduct.getSku());
                if (optionalProduct.isPresent()) {
                    OCBCProductTableTransaction product = optionalProduct.get();
                    if (product.getSourceProductId() != null && product.getSourceProductId() != 0) {
                        orderProductData.setDestinationProductId(product.getSourceProductId());
                    } else {
                        log.info("Given sku is not present in " + secrects.getDestinationClientSystem() + " : " + orderedProduct.getSku());
//                        emailService.sendErrorMail("storeOrderProductDetailsADIS", "The Given SKU " + orderedProduct.getSku() + " for order : " + orderData.getDestinationOrderId() + " is not present in NS ", secrects.getClientName() + Constants.developerMailSub);
//                        emailService.sendAlertMail("The Given SKU : " + orderedProduct.getSku() + " for order : " + orderData.getSourceOrderId() + " is not present in " + secrects.getDestinationClientSystem(), secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
                    }
                } else {
                    log.info("Given line item is not present in the ADIS : " + orderedProduct.getSku());
                    //emailService.sendAlertMail("Given line item is not present in the ADIS : " + orderedProduct.getSku() + " for order : " + orderData.getSourceOrderId() + " is not present in " + secrects.getDestinationClientSystem(), secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
                }
                orderProductData.setBcOrderAddressId(orderedProduct.getOrder_address_id());
                orderProductData.setShippingAddressId(orderedProduct.getOrder_address_id());
                orderProductData.setBasePrice(new BigDecimal(orderedProduct.getBase_price()));
                orderProductData.setBaseTotal(new BigDecimal(orderedProduct.getBase_total()));
                if (bcShippingAddressResponse != null && !bcShippingAddressResponse.isEmpty()) {
                    orderProductData.setShippingFirstname(bcShippingAddressResponse.get(0).getFirst_name());
                    orderProductData.setShippingLastname(bcShippingAddressResponse.get(0).getLast_name());
                    orderProductData.setShippingAddress1(bcShippingAddressResponse.get(0).getStreet_1());
                    orderProductData.setShippingAddress2(bcShippingAddressResponse.get(0).getStreet_2());
                    orderProductData.setShippingCity(bcShippingAddressResponse.get(0).getCity());
                    orderProductData.setShippingState(bcShippingAddressResponse.get(0).getState());
                    orderProductData.setShippingCountry(bcShippingAddressResponse.get(0).getCountry());
                    orderProductData.setShippingZip(bcShippingAddressResponse.get(0).getZip());
                    orderProductData.setShippingCompany(bcShippingAddressResponse.get(0).getCompany());
                    orderProductData.setShippingPhone(bcShippingAddressResponse.get(0).getPhone());
                }

                orderProductData = orderProductRepo.save(orderProductData);
                orderLogServices.storeOrderProductLog(orderProductData.getSourceOrderLineitemId(), orderProductData.getDestinationOrderLineitemId(), OCConstants.success, OCConstants.successReponse, processType, orderProductData.getOrderLineitemDetailsId());
                orderLogServices.storeOrderProductOptionData(orderedProduct, orderProductData.getOrderLineitemDetailsId());

            }
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on ordercommons.BCOrderService.storeOrderProductDetailsADIS  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.storeOrderProductDetailsADIS", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    private String storeShippingDetailsADIS(List<BCOrderShippingResponseDTO> bcShippingAddressResponse, OCBCOrderTableTransaction orderData) {
        log.info("BCOrderservice class -> storeShippingDetailsADIS method started");
        try {
            BCOrderShippingResponseDTO shippingResponse = bcShippingAddressResponse != null && !bcShippingAddressResponse.isEmpty() ? bcShippingAddressResponse.get(0) : new BCOrderShippingResponseDTO();

            OCBCShippingAddressTransaction shippingAddress;
            Optional<OCBCShippingAddressTransaction> optionalShippingAddress = shippingAddressRepo.findByorderID(orderData.getOrderId());

            if (optionalShippingAddress.isPresent()) {
                shippingAddress = optionalShippingAddress.get();
            } else {
                shippingAddress = new OCBCShippingAddressTransaction();
            }

            shippingAddress.setActive(true);
            shippingAddress.setDeleted(false);
            shippingAddress.setShippingAddress1(shippingResponse.getStreet_1());
            shippingAddress.setShippingAddress2(shippingResponse.getStreet_2());
            shippingAddress.setOrderID(orderData.getOrderId());
            shippingAddress.setShippingMethod(shippingResponse.getShipping_method());
            shippingAddress.setShippingCity(shippingResponse.getCity());
            shippingAddress.setShippingCompany(shippingResponse.getCompany());
            shippingAddress.setShippingCountry(shippingResponse.getCountry());
            shippingAddress.setShippingFirstName(shippingResponse.getFirst_name());
            shippingAddress.setShippingLastName(shippingResponse.getLast_name());
            shippingAddress.setShippingPhone(shippingResponse.getPhone());
            shippingAddress.setShippingState(shippingResponse.getState());
            shippingAddress.setShippingZip(shippingResponse.getZip());
            shippingAddress.setCodeExists(true);
            if (bcShippingAddressResponse.get(0).getForm_fields() != null &&
                    !bcShippingAddressResponse.get(0).getForm_fields().isEmpty()) {
                for (BCFormFieldsDTO bcFormFieldsDTO : bcShippingAddressResponse.get(0).getForm_fields()) {
                	log.info("form field name :"+bcFormFieldsDTO.getName());                    
                	if (OCConstants.SHIPTO.equalsIgnoreCase(bcFormFieldsDTO.getName())) {
                       shippingAddress.setShippingAddressCode(bcFormFieldsDTO.getValue());
                    }
                }
            }
            shippingAddressRepo.save(shippingAddress);

            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on ordercommons.BCOrderService.storeShippingDetailsADIS " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.storeShippingDetailsADIS", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }

    }

    public boolean storeQueueResponseInAdis(OrderWebHookDTO queueResponse, String status) {
        try {
            log.info("BCOrderService.storeQueueResponseInAdis method started");
            OCBCOrderWebhookTableTransaction orderWebhook = new OCBCOrderWebhookTableTransaction();
            Optional<OCBCOrderWebhookTableTransaction> webhookResponseFromADIS = bcOrderWebhookTransactionRepository.findByDestinationOrderIdAndStorehash(queueResponse.getData().getId(), secrets.getStorehash());
            orderWebhook.setDestinationOrderId(queueResponse.getData().getId());
            orderWebhook.setScope(queueResponse.getScope());
            boolean saveInADIS = true;
            if (queueResponse.getData().getStatus() != null) {
                orderWebhook.setCurrentOrderStatus(queueResponse.getData().getStatus().getNew_status_id().toString());
                orderWebhook.setPreviousOrderStatus(queueResponse.getData().getStatus().getPrevious_status_id().toString());
            }
            orderWebhook.setStatus(status);
            if (webhookResponseFromADIS.isPresent()) {
                log.info("WebHook data contains Order Update data");
                Optional<OCBCOrderTableTransaction> bcOrderOptional = orderRepo.findByStorehashAndSourceOrderId(secrects.getStorehash(),queueResponse.getData().getId());
                if(bcOrderOptional.isPresent()) {
                	if(bcOrderOptional.get().getIntegrationStatus().equals(OCConstants.completed)&&!orderWebhook.getPreviousOrderStatus().equals("12")) {
                		saveInADIS = false;
                	}
                }
                orderWebhook.setOrderWebhookLogId(webhookResponseFromADIS.get().getOrderWebhookLogId());
                orderWebhook.setRetryCount(webhookResponseFromADIS.get().getRetryCount());
            } else {
                log.info("WebHook data contains Order Create data");
                orderWebhook.setRetryCount(0);
            }
            orderWebhook.setStorehash(secrects.getStorehash());
            if(saveInADIS) {
              bcOrderWebhookTransactionRepository.save(orderWebhook);
            }
            log.info("BCOrderService.storeQueueResponseInAdis method ended");
            return true;
        } catch (Exception e) {
            log.error("Exception Occured on method ordercommons.BCOrderService.storeQueueResponseInAdis  :" + e.toString());
            emailService.sendErrorMail("ordercommons.BCOrderService.storeQueueResponseInAdis", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return false;
        }

    }

    public String fetchOrderReportData(String accessKey, String secretKey, String region, String activeProfile) {
        log.info("ordercommons.BCOrderService.fetchOrderReportData method get started  ");
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDateTime now = LocalDateTime.now();
            String rawDate = dtf.format(now);
            log.info("localDataTime  : " + rawDate);
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(rawDate);
            Calendar cal7 = Calendar.getInstance();
            cal7.setTime(date);
            cal7.add(Calendar.MINUTE, -60);
            Date oneHourBack = cal7.getTime();
            Timestamp startDate = new java.sql.Timestamp(oneHourBack.getTime());
            log.info("startData  : " + startDate);
            List<OCBCOrderTransactionLog> orderLogDetails = orderLogRepo.getCustomerLogDetails(startDate);
            if (orderLogDetails.isEmpty()) {
                return "No order data found for the given time ";
            }

            Map<Integer, List<OCBCOrderTransactionLog>> orderGroupByIdMap = orderLogDetails.stream().collect(Collectors.groupingBy(OCBCOrderTransactionLog::getSourceOrderId));

            List<OrderReportFieldsDTO> orderReportFieldsList = new ArrayList<>();
            for (Map.Entry<Integer, List<OCBCOrderTransactionLog>> entry : orderGroupByIdMap.entrySet()) {
                Optional<OrderReportFieldsDTO> optionalReportFields = settingReportFields(entry.getValue());
                if (optionalReportFields.isPresent()) {
                    orderReportFieldsList.add(optionalReportFields.get());
                }
            }
            return writeReportFile(orderReportFieldsList, accessKey, secretKey, region, activeProfile);

        } catch (ParseException e) {
            log.error("Exception on ordercommons.BCOrderService.fetchOrderReportData  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.fetchOrderReportData", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }



	private Optional<OrderReportFieldsDTO> settingReportFields(List<OCBCOrderTransactionLog> orderLogList) {
        try {
            log.info("ordercommons.BCOrderService.settingReportFields method started");
            OrderReportFieldsDTO reportFields = new OrderReportFieldsDTO();

            orderLogList.sort(Comparator.comparing(OCBCOrderTransactionLog::getSourceOrderId));
            OCBCOrderTransactionLog logEntity = orderLogList.get(orderLogList.isEmpty() ? null : (orderLogList.size() - 1));
            if (logEntity == null) {
                return Optional.empty();
            }

            reportFields.setSourceOrderID(String.valueOf(logEntity.getSourceOrderId()));
            reportFields.setDestinationOrderID(String.valueOf(logEntity.getDestinationOrderId()));
            reportFields.setOrderStatus(logEntity.getStatus());
            if (logEntity.getStatus().equalsIgnoreCase(OCConstants.success)) {
                reportFields.setNotes("Successfully integrated");
            } else {
                reportFields.setNotes(logEntity.getResponse());
            }
            reportFields.setIntegrationType(logEntity.getType());
            reportFields.setInterfaceType(logEntity.getInterfacetype());

            return Optional.of(reportFields);
        } catch (Exception e) {
            log.error("Excpetion on ordercommons.BCOrderService.settingReportFields  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.settingReportFields ", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return Optional.empty();
        }
    }

    private String writeReportFile(List<OrderReportFieldsDTO> orderReportList, String accessKey, String secretKey, String region, String activeProfile) {
        try {
            log.info("ordercommons.BCOrderService.writeReportFile method started");
            File reportFileObj = File.createTempFile("aws-java-sdk-", ".xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet();
            String[] orderHeaders = OCConstants.secrects.getOrderHeaders().split(",");
            Row headerRow = spreadsheet.createRow(0);
            for (int col = 0; col < orderHeaders.length; col++) {
                headerRow.createCell(col).setCellValue(orderHeaders[col]);
            }
            log.info("list of excel data:" + orderReportList.size());
            int rowIndex = 1;
            int successCount = 0;
            for (OrderReportFieldsDTO data : orderReportList) {
                Row row = spreadsheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rowIndex - 1);
                row.createCell(1).setCellValue(data.getSourceOrderID());
                row.createCell(2).setCellValue(data.getDestinationOrderID());
                row.createCell(3).setCellValue(data.getOrderStatus());
                row.createCell(4).setCellValue(data.getNotes());
                if (data.getOrderStatus().equalsIgnoreCase(OCConstants.success)) {
                    successCount++;
                }
                row.createCell(5).setCellValue(data.getInterfaceType());
                row.createCell(6).setCellValue(data.getIntegrationType());
            }

            String fileName = OCConstants.secrects.getClientName() + OCConstants.orderReportFileName + OCConstants.excelExtention;
            String sub = secrects.getClientName() + OCConstants.ORDER_REPORT_MAIL_SUB;
            String frontContent = "Please find the order integration report details";
            try (FileOutputStream fileOutputStream = new FileOutputStream(reportFileObj)) {
                workbook.write(fileOutputStream);
            }

            s3Service.saveToS3Bucket(activeProfile, region, accessKey, secretKey, reportFileObj, fileName);
            
            emailService.reportMail(frontContent, reportFileObj, fileName, successCount, sub);
            return OCConstants.success;
        } 
        catch (IOException e) {
            log.error("Exception on ordercommons.BCOrderService.writeReportFile  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("writeReportFile", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    public String getOrdersByModifiedData(String minModifiedTime) {
        log.info("Entered into ordercommons.BCOrderService.getOrdersByModifiedData method ");
        try {
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            int pageCount = 1;
            feign.Response feignResponse;
            do {
                feignResponse = bcRestClientBuilder.getOrderByModifiedTime(secrects.getAccessToken(), secrects.getStorehash(), minModifiedTime, pageCount);
                pageCount++;
                if (feignResponse.status() == 200) {
                    String bcOrderReponseBody = IOUtils.toString(feignResponse.body().asReader());
                    JSONArray orderArray = new JSONArray(bcOrderReponseBody);
                    for (Object orderObject : orderArray) {

                        JSONObject orderJson = (JSONObject) orderObject;
                        OCBCOrderWebhookTableTransaction webHookData;
                        Optional<OCBCOrderWebhookTableTransaction> optionalWebHookData = webRepo.findByDestinationOrderIdAndStorehash(orderJson.getInt("id"), secrets.getStorehash());
                        if (optionalWebHookData.isPresent()) {
                            webHookData = optionalWebHookData.get();
                        } else {
                            webHookData = new OCBCOrderWebhookTableTransaction();
                        }

                        webHookData.setActive(true);
                        webHookData.setDeleted(false);
                        webHookData.setDestinationOrderId(orderJson.getInt("id"));
                        webHookData.setScope("BC Order API scope");
                        webHookData.setStatus(OCConstants.PendingStatus);
                        webRepo.save(webHookData);
                    }
                }
            } while (feignResponse.status() != 200);

            return OCConstants.success;
        } catch (IOException | JSONException e) {
            log.error("Exception on ordercommons.BCOrderService.getOrdersByModifiedData  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("ordercommons.BCOrderService.getOrdersByModifiedDataF", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub);
            return OCConstants.failed;
        }
    }

    public String changeOrderStatusBC(Integer statusID, Integer sourceOrderID) {
        log.info("Entered into BCOrderServices.changeOrderStatusBC method");
        try {
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("status_id", statusID);
            log.info("BC status change request : " + jsonRequest.toString());
            feign.Response feignResponse = bcRestClientBuilder.updateOrder(secrects.getAccessToken(), secrects.getStorehash(), sourceOrderID, jsonRequest.toString());
            if (feignResponse.status() == 200) {
                log.info("Status changed successfully");
                emailService.sendAlertMail("Changed the status of the order to 'Manual Verification Required' for order : " + sourceOrderID, secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
            } else {
                log.info("Failed to update the status : " + statusID + " for order : " + sourceOrderID);
                emailService.sendAlertMail("Failed to update the status : " + statusID + " for order : " + sourceOrderID, secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
//                emailService.sendErrorMail("ordercommons.BCOrderService.changeOrderStatusBC", "Failed to update the status : " + statusID + " for order : " + sourceOrderID, secrects.getClientName() + Constants.developerMailSub);
            }
            return OCConstants.success;
        } catch (JSONException e) {
            log.error("Exception on BCOrderServices.changeOrderStatusBC method : " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }

    public String updateOrderCustomerDetails(int customerId, int sourceOrderID) {
        try {
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("customer_id", customerId);
            log.info("BC customer id change request : " + jsonRequest.toString());
            feign.Response feignResponse = bcRestClientBuilder.updateOrder(secrects.getAccessToken(), secrects.getStorehash(), sourceOrderID, jsonRequest.toString());
            if (feignResponse.status() == 200) {
                log.info("Status changed successfully");
//                emailService.sendAlertMail("Updated the Customer Id for this order in Bc: " + sourceOrderID+", Customer Id : "+customerId, secrects.getClientName() + Constants.developerMailSub, secrects.getClientMailTo());
            } else {
                log.info("Failed to update the customer Id in Bc for order : " + sourceOrderID + ", Customer Id : " + customerId);
//                emailService.sendAlertMail("Failed to update the customer Id in Bc for order : " + sourceOrderID+", Customer Id : "+customerId, secrects.getClientName() + Constants.developerMailSub, secrects.getClientMailTo());

            }

            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on BCOrderServices.updateOrderCustomerDetails method : " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }
    
    
    public String UpdateStaffNotes(String staffNotes , Integer sourceOrderID) {
        log.info("Entered into BCOrderServices.changeOrderStatusBC method");
        try {
            OCBigcommerceClient bcRestClientBuilder = clientBuilder.createSecureClient(OCBigcommerceClient.class, secrects.getBcBaseUrl());
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("staff_notes", staffNotes);
            log.info("BC status change request : " + jsonRequest.toString());
            feign.Response feignResponse = bcRestClientBuilder.updateOrder(secrects.getAccessToken(), secrects.getStorehash(), sourceOrderID, jsonRequest.toString());
            if (feignResponse.status() == 200) {
                log.info("Status changed successfully");
               // emailService.sendAlertMail("Changed the status of the order to 'Manual Verification Required' for order : " + sourceOrderID, secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
            } else {
                log.info("Failed to update the status : " + staffNotes + " for order : " + sourceOrderID);
               // emailService.sendAlertMail("Failed to update the status : " + CustomerMessage + " for order : " + sourceOrderID, secrects.getClientName() + OCConstants.developerMailSub, secrects.getClientMailTo());
//                emailService.sendErrorMail("ordercommons.BCOrderService.changeOrderStatusBC", "Failed to update the status : " + statusID + " for order : " + sourceOrderID, secrects.getClientName() + Constants.developerMailSub);
            }
            return OCConstants.success;
        } catch (JSONException e) {
            log.error("Exception on BCOrderServices.changeOrderStatusBC method : " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }
    

}
