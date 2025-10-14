package com.arizon.racetrac.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arizon.ordercommons.repository.OCBCOrderProductTransactionRepository;
import com.arizon.ordercommons.repository.OCBCOrderTransactionRepository;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class RacetracInventoryService {

    @Autowired
    private OCBCOrderTransactionRepository ocbcOrderTransactionRepository;

    @Autowired
    private OCBCOrderProductTransactionRepository ocbcOrderProductTransactionRepository;

    // SFTP Credentials
    private final String host = "sftp.racetrac.com";
    private final int port = 22;
    private final String user = "strtsftpprodeastus001.wdoutboundint.wdoutboundint";
    private final String password = "WH6tS6qy61hq+TK0gTi1KZwPy7L3Q4XI";
    private final String remoteDir = "/SupplyChain/WDtoOmniPortal_Outbound/Inventory/";

    public void integrateInventoryDetails() {
        log.info("Inventory Integration Intitated ");

                Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = setupSftpSession();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            log.info("SFTP Channel connected successfully.");

            channelSftp.cd(remoteDir);
            Vector<ChannelSftp.LsEntry> files = channelSftp.ls("*.json");

            if (files.isEmpty()) {
                log.info("No JSON files found in {}", remoteDir);
                return;
            }

            for (ChannelSftp.LsEntry entry : files) {
                String fileName = entry.getFilename();
                log.info("Processing file: {}", fileName);
                String jsonContent = readFileFromSftp(channelSftp, fileName);

               // processShipmentJson(jsonContent);
                processInventoryJson(jsonContent);
            }

        } catch (Exception e) {
            log.error("Error processing SFTP files: {}", e.getMessage(), e);
        } finally {
            disconnect(channelSftp, session);
        }    
    }

     // --- SFTP Helper Methods ---
    private Session setupSftpSession() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        log.info("Connecting to SFTP server: {}", host);
        session.connect();
        log.info("SFTP Session connected successfully.");
        return session;
    }

    private String readFileFromSftp(ChannelSftp channelSftp, String fileName) throws Exception {
        try (InputStream inputStream = channelSftp.get(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            return jsonContent.toString();
        }
    }

    private void disconnect(ChannelSftp channelSftp, Session session) {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
            log.info("SFTP Channel disconnected.");
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("SFTP Session disconnected.");
        }
    }
 
     //List of orders
//    private void processInventoryJson(String jsonContent) {
//        JSONObject json = new JSONObject(jsonContent);
//
//        if (!json.has("inventoryReport")) {
//            log.warn("No inventoryReport found in JSON");
//            return;
//        }
//
//        JSONArray inventoryReports = json.getJSONArray("inventoryReport");
//
//        for (int i = 0; i < inventoryReports.length(); i++) {
//            JSONObject report = inventoryReports.getJSONObject(i);
//
//            if (!report.has("inventoryItemLocationInformation")) {
//                log.warn("No inventoryItemLocationInformation found in report {}", i);
//                continue;
//            }
//
//            JSONArray itemLocations = report.getJSONArray("inventoryItemLocationInformation");
//
//            for (int j = 0; j < itemLocations.length(); j++) {
//                JSONObject itemLocation = itemLocations.getJSONObject(j);
//
//                if (!itemLocation.has("inventoryLocation")) {
//                    log.warn("No inventoryLocation found in itemLocation {}", j);
//                    continue;
//                }
//
//                JSONObject inventoryLocation = itemLocation.getJSONObject("inventoryLocation");
//                String warehouseId = inventoryLocation.getString("primaryId");
//
//                log.info("Extracted Warehouse ID: {}", warehouseId);
//
//                // You can now continue extracting SKU, inventory levels, etc., like this:
//                JSONObject tradeItem = itemLocation.getJSONObject("transactionalTradeItem");
//                String sku = tradeItem.getString("primaryId");
//
//                JSONArray lineItems = itemLocation.getJSONArray("lineItem");
//
//                for (int k = 0; k < lineItems.length(); k++) {
//                    JSONObject lineItem = lineItems.getJSONObject(k);
//
//                    JSONArray statusQtySpecs = lineItem.getJSONArray("inventoryStatusQuantitySpecification");
//                    for (int l = 0; l < statusQtySpecs.length(); l++) {
//                        JSONObject status = statusQtySpecs.getJSONObject(l);
//                        int inventoryLevel = status.getJSONObject("quantityOfUnits").getInt("value");
//
//                        log.info("SKU: {}, Warehouse ID: {}, Inventory Level: {}", sku, warehouseId, inventoryLevel);
//
//                        // 🔁 You can store this in DB, update inventory, etc.
//                    }
//                }
//            }
//        }
//    }

    
    private void processInventoryJson(String jsonContent) {
        JSONObject json = new JSONObject(jsonContent);

        if (!json.has("inventoryReport")) {
            log.warn("No inventoryReport found in JSON");
            return;
        }

        JSONArray inventoryReports = json.getJSONArray("inventoryReport");

        if (inventoryReports.isEmpty()) {
            log.warn("inventoryReport array is empty.");
            return;
        }

        // ✅ Process ONLY the first order
        JSONObject report = inventoryReports.getJSONObject(0);

        if (!report.has("inventoryItemLocationInformation")) {
            log.warn("No inventoryItemLocationInformation found in first report");
            return;
        }

        JSONArray itemLocations = report.getJSONArray("inventoryItemLocationInformation");

        for (int j = 0; j < itemLocations.length(); j++) {
            JSONObject itemLocation = itemLocations.getJSONObject(j);

            if (!itemLocation.has("inventoryLocation")) {
                log.warn("No inventoryLocation found in itemLocation {}", j);
                continue;
            }

            JSONObject inventoryLocation = itemLocation.getJSONObject("inventoryLocation");
            String warehouseId = inventoryLocation.getString("primaryId");
            

            log.info("Extracted Warehouse ID: {}", warehouseId);

            JSONObject tradeItem = itemLocation.getJSONObject("transactionalTradeItem");
            String sku = tradeItem.getString("primaryId");
            

            JSONArray lineItems = itemLocation.getJSONArray("lineItem");

            for (int k = 0; k < lineItems.length(); k++) {
                JSONObject lineItem = lineItems.getJSONObject(k);

                JSONArray statusQtySpecs = lineItem.getJSONArray("inventoryStatusQuantitySpecification");
                for (int l = 0; l < statusQtySpecs.length(); l++) {
                    JSONObject status = statusQtySpecs.getJSONObject(l);
                    int inventoryLevel = status.getJSONObject("quantityOfUnits").getInt("value");

                    log.info("SKU: {}, Warehouse ID: {}, Inventory Level: {}", sku, warehouseId, inventoryLevel);
               
                   
                    
                }
            }
        }
    }

}
