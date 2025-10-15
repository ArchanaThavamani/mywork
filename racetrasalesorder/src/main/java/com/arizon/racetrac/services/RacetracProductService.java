package com.arizon.racetrac.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.arizon.productcommon.config.PCConstants;
import com.arizon.productcommon.entity.PCProductTransaction;
//import com.arizon.productcommon.repository.PCProductCustomFeildTransactionRepository;
import com.arizon.productcommon.repository.PCProductTransactionRepository;
import com.arizon.racetrac.config.RacetracAwsSecrets;
import com.arizon.racetrac.model.PurchaseItemData;
import com.arizon.racetrac.model.PurchaseItemResponse;
import com.arizon.racetrac.restclient.WorkdayRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;


@Service
public class RacetracProductService {

    private static final Logger log = LoggerFactory.getLogger(RacetracProductService.class);

    @Autowired
    private WorkdayRestClient workdayRestClient;

    @Autowired
    private PCProductTransactionRepository productRepo;

//    @Autowired
//    private PCProductCustomFeildTransactionRepository customFieldRepo;

    @Autowired
    private RacetracAwsSecrets awsSecrets;

    public String syncProductsFromWorkdayToADIS() throws JsonProcessingException {
        String accessToken = getAccessToken();

        List<PurchaseItemData> products = fetchPurchaseItems(accessToken);

        log.info("Total Products Fetched: {}", products.size());

        for (PurchaseItemData product : products) {
           // processProduct(product);
        }
        return "success";
    }

    private String getAccessToken() {
        Map<String, String> form = Map.of(
            "client_id", awsSecrets.getWorkdayClientId(),
            "client_secret", awsSecrets.getWorkdayClientSecret(),
            "grant_type", awsSecrets.getWorkdayGrantType(),
            "refresh_token", awsSecrets.getWorkdayRefreshToken()
        );
        return workdayRestClient.getToken("application/json", form).getAccessToken();
    }

    private List<PurchaseItemData> fetchPurchaseItems(String accessToken) {
        String query =
    "SELECT purchaseItem,itemName,itemDescription,itemIdentifier,purchaseItemGroup," +
    "baseUnitOfMeasure,preferredItem as Preferred,spendCategoryForItem," +
    "alternateItemIdentifiersForItem,unitPrice,manufacturerName," +
    "purchaseItemStatus,lastUpdateTimestamp FROM purchaseItemsIndexed LIMIT 3";

        ResponseEntity<PurchaseItemResponse> response =
                workdayRestClient.getPurchaseItems(query, "application/json",
                        awsSecrets.getWorkdayClientId(), "Bearer " + accessToken);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch purchase items: " + response.getStatusCode());
        }

        return response.getBody().getData();
    }

//    private void processProduct(PurchaseItemData productData) {
//        String storehash = PCConstants.secrets.getStorehash();
//        String sku = productData.getItemIdentifier();
//
//        PCProductTransaction product = productRepo
//                .findFirstByStorehashAndSku(storehash, sku)
//                .map(existing -> ProductMapper.updateEntity(existing, productData))
//                .orElseGet(() -> ProductMapper.toEntity(productData));
//
//        product = productRepo.save(product);
//
//        processCustomField(product, productData);
//    }

//    private void processCustomField(PCProductTransaction product, PurchaseItemData productData) {
//        customFieldRepo.findByProductIdAndName(BigInteger.valueOf(product.getProductId()), "spendCategoryForItem")
//                .ifPresentOrElse(
//                    field -> {
//                        field.setValue(productData.getSpendCategoryForItem().getDescriptor());
//                        field.setStatus(PCConstants.PENDING);
//                        field.setActive(true);
//                        customFieldRepo.save(field);
//                    },
//                    () -> customFieldRepo.save(ProductMapper.toCustomField(productData, product.getProductId()))
//                );
//    }
}
