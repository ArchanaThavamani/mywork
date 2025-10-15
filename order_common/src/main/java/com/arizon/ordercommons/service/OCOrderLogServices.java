/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.service;

import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.entity.OCBCOrderProductOptionTableTransaction;
import com.arizon.ordercommons.entity.OCBCOrderProductOptionTransactionLog;
import com.arizon.ordercommons.entity.OCBCOrderProductTransactionLog;
import com.arizon.ordercommons.entity.OCBCOrderTransactionLog;
import com.arizon.ordercommons.entity.OCProductOptionTransaction;
import com.arizon.ordercommons.entity.OCProductOptionValueTransaction;
import com.arizon.ordercommons.model.BCOrderProductOptionDTO;
import com.arizon.ordercommons.model.BCOrderProductResponseDTO;
import com.arizon.ordercommons.repository.OCBCOrderProductOptionTransactionLogRepository;
import com.arizon.ordercommons.repository.OCBCOrderProductOptionTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderProductTransactionLogRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionLogRepository;
import com.arizon.ordercommons.repository.OCProductOptionTransactionRepository;
import com.arizon.ordercommons.repository.OCProductOptionValueTransactionRepository;
import com.arizon.ordercommons.util.OrderUtility;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Service
@Slf4j
public class OCOrderLogServices {

    @Autowired
    private OCBCOrderTransactionLogRepository orderLogRepo;

    @Autowired
    private OCBCOrderProductOptionTransactionLogRepository orderProductOptionLogRepo;

    @Autowired
    private OCBCOrderProductTransactionLogRepository orderProductLogRepo;

    @Autowired
    private OCEmailServices emailService;
    @Autowired
    OCProductOptionValueTransactionRepository productOptionvalueRepo;

    @Autowired
    OCBCOrderProductOptionTransactionRepository orderproductOptionRepo;

    @Autowired
    OCProductOptionTransactionRepository productOptionRepo;

    public String storeOrderLogDetails(int orderID, String destinationOrderID, String status, String request, String response, String type, int sourceOrderID, String interfaceType) {
        try {

            OCBCOrderTransactionLog orderLog = new OCBCOrderTransactionLog();
            orderLog.setOrderId(orderID);
            orderLog.setDestinationOrderId(destinationOrderID);
            orderLog.setStatus(status);
            orderLog.setRequest(request);
            orderLog.setResponse(response);
            orderLog.setType(type);
            orderLog.setSourceOrderId(sourceOrderID);
            orderLog.setInterfacetype(interfaceType);
            orderLogRepo.save(orderLog);
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on OrderLogServices.storeOrderLogDetails  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("OrderLogServices.storeOrderLogDetails", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }
    }

    public String storeProductOptionLog(int orderLineItemOptionID, int sourceOrderLineItemOptionID, String status, String response, String type, int destinationOrderLineItemOptionID) {
        try {
            OCBCOrderProductOptionTransactionLog productOptionLog = new OCBCOrderProductOptionTransactionLog();
            productOptionLog.setDestinationOrderLineitemOptionId(destinationOrderLineItemOptionID);
            productOptionLog.setOrderLineitemOptionDetailsId(orderLineItemOptionID);
            productOptionLog.setResponse(response);
            productOptionLog.setSourceOrderLineItemOptionID(sourceOrderLineItemOptionID);
            productOptionLog.setStatus(status);
            productOptionLog.setType(type);
            orderProductOptionLogRepo.save(productOptionLog);
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on OrderLogServices.storeProductOptionLog  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("OrderLogServices.storeProductOptionLog", e.getMessage(), secrects.getClientName() + OCConstants.developerMailSub + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }
    }

    public String storeOrderProductLog(int sourceOrderLineItemID, int destinationLineItemID, String status, String response, String type, int orderLineItemDetailsID) {
        try {
            OCBCOrderProductTransactionLog orderedProductLog = new OCBCOrderProductTransactionLog();

            orderedProductLog.setDestinationOrderLineitemId(destinationLineItemID);
            orderedProductLog.setOrderLineitemDetailsId(orderLineItemDetailsID);
            orderedProductLog.setResponse(response);
            orderedProductLog.setType(type);
            orderedProductLog.setSourceOrderLineitemId(sourceOrderLineItemID);
            orderedProductLog.setStatus(status);

            orderProductLogRepo.save(orderedProductLog);
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on OrderLogServices.storeOrderProductLog  " + OrderUtility.getStackTrace(e));
            emailService.sendErrorMail("OrderLogServices.storeOrderProductLog", e.getMessage(), OCConstants.developerMailSub + secrects.getClientName() + OrderUtility.getCurrentData());
            return OCConstants.failed;
        }
    }

    public String storeOrderProductOptionData(BCOrderProductResponseDTO orderedProduct, Integer orderLineitemDetailsId) {
        try {
            log.info("Stored order product option data in ADIS table method started");

            for (BCOrderProductOptionDTO orderproductOption : orderedProduct.getProduct_options()) {
                log.info("Order line item option data" + orderproductOption);
                OCBCOrderProductOptionTableTransaction orderProductOptionData = new OCBCOrderProductOptionTableTransaction();
                orderProductOptionData.setSku(orderedProduct.getSku());
                orderProductOptionData.setProductOptionsName(orderproductOption.getDisplay_name());
                Optional<OCProductOptionTransaction> productOption = productOptionRepo.findByDestinationOptionIDAndIsActiveAndIsDeleted(orderproductOption.getOption_id(), true, false); //correct checked in ADIS table.
                if (productOption.isPresent()) {
                    orderProductOptionData.setProduct_options_id(productOption.get().getProductOptionsID());
                }
                if (Integer.parseInt(orderproductOption.getValue()) != 0) {
                    Optional<OCProductOptionValueTransaction> productOptionValue = productOptionvalueRepo.findByDestinationOptionValueIDAndIsActiveAndIsDeleted(Integer.parseInt(orderproductOption.getValue()), true, false);
                    if (productOptionValue.isPresent()) {
                        orderProductOptionData.setProduct_option_value_id(productOptionValue.get().getProductOptionValueID());
                    }
                }
                orderproductOption.getDisplay_value().replace("&amp;", "&");
                orderproductOption.getDisplay_value().replace("&lt;", "<");
                orderproductOption.getDisplay_value().replace("&gt;", ">");

                orderProductOptionData.setProductOptionValueName(orderproductOption.getDisplay_value());
                orderProductOptionData.setActive(true);
                orderProductOptionData.setDeleted(false);
                orderProductOptionData.setSourceOrderLineitemOptionId(orderproductOption.getId());

                Optional<OCBCOrderProductOptionTableTransaction> optionalorderProductOption = orderproductOptionRepo.findBySourceOrderLineitemOptionIdAndOrderLineitemDetailsId(orderproductOption.getId(), orderLineitemDetailsId);
                String type;
                if (optionalorderProductOption.isPresent()) {
                    orderProductOptionData.setOrderLineitemDetailsId(orderLineitemDetailsId);
                    orderProductOptionData.setDestinationOrderLineitemOptionId(optionalorderProductOption.get().getDestinationOrderLineitemOptionId());
                    orderProductOptionData.setOrderLineitemOptionDetailsId(optionalorderProductOption.get().getOrderLineitemOptionDetailsId());
                    orderProductOptionData = orderproductOptionRepo.save(orderProductOptionData);
                    type = OCConstants.update;
                } else {
                    orderProductOptionData.setOrderLineitemDetailsId(orderLineitemDetailsId);
                    orderProductOptionData.setDestinationOrderLineitemOptionId(0);
                    orderProductOptionData = orderproductOptionRepo.save(orderProductOptionData);
                    type = OCConstants.insert;
                }
                storeProductOptionLog(orderProductOptionData.getOrderLineitemOptionDetailsId(), orderProductOptionData.getSourceOrderLineitemOptionId(), OCConstants.success, OCConstants.successReponse, type, orderProductOptionData.getDestinationOrderLineitemOptionId());

            }
        } catch (Exception e) {
            log.info("Exception occured when store order product option data in ADIS table" + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
        log.info("Stored order product option data in ADIS table method started");
        return OCConstants.success;
    }
}
