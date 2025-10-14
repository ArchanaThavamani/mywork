package com.arizon.racetrac.services;

import com.arizon.racetrac.restclient.WorkdayRestClient; // existing Feign client for token
import com.arizon.racetrac.restclient.BlueyonderOrderClient;
import com.arizon.racetrac.model.TokenResponse;
import com.arizon.racetrac.restclient.BlueyonderRestClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RacetracTestOrderService {

    @Autowired
    private BlueyonderRestClient blueyonderRestClient; // existing Feign client used to get token

    @Autowired
    private BlueyonderOrderClient blueyonderOrderClient;

    public void integrateTestSalesOrdersToBlueyonder() {
        try {
            log.info("Starting BlueYonder Sales Order Integration...");

            // 🔹 STEP 1: Get Token (same logic as your working getAccessToken)
            Map<String, String> form = Map.of(
                "client_id", "your-client-id",
                "client_secret", "your-client-secret",
                "grant_type", "your-grant-type",
                "refresh_token", "your-refresh-token"
            );

            // TokenResponse tokenResponse = blueyonderRestClient.getToken("application/json", form);
            String accessToken = blueyonderRestClient.getAccessToken();
            //String accessToken = tokenResponse.getAccessToken();
            log.info(" Access token retrieved successfully.");

            // 🔹 STEP 2: Hardcoded order JSON
            String orderJson = """
                {
                  "orderSubType": "C",
                  "orderLogisticalInformation": {
                    "shipFrom": { "primaryId": "1145" },
                    "orderLogisticalDateInformation": {
                      "requestedDeliveryDateRange": {
                        "beginDate": "2025-10-08",
                        "endDate": "2025-10-08",
                        "beginTime": "00:01:00.000",
                        "endTime": "23:59:00.000"
                      },
                      "requestedShipDateRange": {
                        "beginDate": "2025-10-08",
                        "endDate": "2025-10-08",
                        "beginTime": "00:01:00.000",
                        "endTime": "23:59:00.000"
                      }
                    },
                    "shipTo": { "primaryId": "185" }
                  },
                  "orderId": "Test",
                  "documentStatusCode": "ORIGINAL",
                  "entityId": "Test",
                  "documentActionCode": "ADD",
                  "buyer": { "primaryId": "185" },
                  "tmCustomerCode": "RTD",
                  "lineItem": [
                    {
                      "orderLinePriority": "2",
                      "isSaturdayDeliveryAllowed": true,
                      "shipmentSplitMethod": "NO_SPLIT",
                      "lineItemDetail": [
                        {
                          "orderLogisticalInformation": {
                            "orderLogisticalDateInformation": {
                              "requestedDeliveryDateRange": {
                                "beginDate": "2025-02-12",
                                "endDate": "2025-02-12",
                                "beginTime": "01:01:00.000",
                                "endTime": "18:59:00.000"
                              },
                              "requestedShipDateRange": {
                                "beginDate": "2025-02-12",
                                "endDate": "2025-02-12",
                                "beginTime": "01:01:00.000",
                                "endTime": "17:59:00.000"
                              }
                            }
                          },
                          "requestedQuantity": { "value": 5 }
                        }
                      ],
                      "transactionalTradeItem": {
                        "additionalTradeItemId": [
                          { "value": "00000000000000", "typeCode": "BUYER_ASSIGNED" }
                        ],
                        "primaryId": "11013"
                      },
                      "requestedQuantity": { "value": 5 },
                      "lineItemNumber": 10,
                      "isCaseSplittingAllowed": true
                    }
                  ]
                }
                """;

            // 🔹 STEP 3: Call BlueYonder API
            String bearerToken = "Bearer " + accessToken;
            ResponseEntity<String> response = blueyonderOrderClient.postOrder(bearerToken, orderJson);

            log.info("BlueYonder API Response: {}", response.getBody());
            log.info("HTTP Status: " + response.getStatusCode());

        } catch (Exception ex) {
            log.error("Error while integrating order to BlueYonder: ", ex);
        }
    }
}
