/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.configs;

import lombok.Data;

/**
 *
 * @author kavinkumar.s
 */
@Data
public class OCConstants {

    private OCConstants() {
    }
    public static final String PendingStatus = "Pending";
    public static final String HoldStatus = "Hold";
    public static final String WebhookInProgressStatus = "InProgress";
    public static final String WebhookCompletedStatus = "Completed";
    public static final String completed = "Completed";
    public static final String WebhookFailedStatus = "Failed";
    public static final String OrderWebhookScope = "store/order/statusUpdated";
    public static final String Thanks = "Thanks";
    public static final String Arizon = "Arizon";
    public static final String smtpAuth = "mail.smtp.auth";
    public static final String smtpHost = "mail.smtp.host";
    public static final String smtpPort = "mail.smtp.port";
    public static final String smtpenable = "mail.smtp.starttls.enable";
    public static final String Content = "text/html";
    public static final String insert = "Insert";
    public static final String update = "Update";
    public static final String create="Create";
    public static final String BCtoADIS = "Order data successfully saved BigCommerce to ADIS table";
    public static final String AdisSuccess = "ADISSuccess";
    public static final String success = "Success";
    public static final String failed = "Failed";
    public static final String WebhookPendingStatus = "Pending";
    public static OCAwsSecrets secrects;
    public static final String EDINAME = "EDI850";
    public static final String RESPONSE_PREFIX = "\"$$FROM_GP:{";
    public static final String RESPONSE_SUFFIX = "}$$\"";

    public static final String orderReportFileName = " Order Report";
    public static final String excelExtention = ".xlsx";

    public static final String nsInterface = "ADIS To NetSuite";
    public static final String bcInterface = "BigCommerce To ADIS";
    public static final String adisToGPInteface="ADIS to GP";
    public static final String GPInterface = "GP to Big Commerce";

    public static final String successReponse = "Successfully integrated";
    public static final String developerMailSub = " - Order Developer Mail Alert";
    public static final String currentDateFormat = "dd-MM-yyyy HH-mm-ss";

    public static final String STATE = "state";
    public static final String COUNTRY = "country";

    public static int emptyQueueCount = 0;
    public static int minuteOfExecution = 10;
    public static final String B2C_CUSTOMER = "B2C";

    public static final String NORMAL_PROCESS = "noraml_process";
    public static final String RETRY_PROCESS = "retry_process";
    public static int cancelStatusId = 5;
    public static String cancelOrderstatus = "cancelled";
    public static final String salesToBack = "salesOrderToBackOrder";
    public static final String backToSales = "BackOrderToSalesOrder";
    public static final String gpPending = "GPPending";
    public static final String clientName="Connolly";
    public static final String DEVELOPER_MAIL_SUB = "Order Integration Error Mail Alert";
    public static final String ORDER_REPORT_MAIL_SUB = "Order Integration Report";
    public static final String ORDER_REPORT_MAIL_FRONT_CONTENT = "Please find the order integration report details";
    public static final String SHIPTO = "ShipToParty_ID";

}
