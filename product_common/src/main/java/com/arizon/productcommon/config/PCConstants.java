/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.config;

import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;

/**
 *
 * @author mohan.e
 */
@Service
@Data
@ToString
//@NoArgsConstructor
public class PCConstants {

    private PCConstants() {

    }
    public static PCAwsSecrets secrets;
    public static String LF_NEW_LINE = "\n";
    public static String CR_NEW_LINE = "\r";
    public static String CRLF_NEW_LINE = "\r\n";
    public static final String PENDING = "Pending";
    public static final String FAILED = "Failed";
    public static final String INPROGRESS = "IN-PROGRESS";
    public static final String NEWPRODUCT = "NEWPRODUCT";
    public static final String NEWCATEGORY = "NEWCATEGORY";
    public static final String GC = "GC";
    public static final String LC = "LC";
    public static final int ZEROS = 0;
    public static final BigDecimal ZERODECIMAL = new BigDecimal("0");
    public static final BigDecimal DEFAULTPRICE = new BigDecimal("0.1");
    public static final String SUCCESS = "Success";
    public static final String CREATE = "Create";
    public static final String UPDATE = "Update";
    public static final String options = "Packaging,Size,Gauge,Color-Quality-Material,Instrument,Style,String-Set";//need to change aws secrets.
    public static final String AdisInsertResposne = "Successfully Inserted";
    public static final String AdisUpdateResposne = "Successfully Updated";
    public static final String INSERT = "Insert";
    public static final String DELETE = "Delete";
    public static final String APPLICATION_STARTED = "started";
    public static final String APPLICATION_ENDED = "ended";
    public static String STATUS_MAIL_SUB = "Product Integration Status Mail ";
    public static final double ZERO = 0.0;
//    public static final String MacModStoreHash = "";
//    public static final String ConnollyStoreHash = "";
    public static final String excelExtention = ".xlsx";
    public static final String LISTPRICE = "LISTPRICE";
    public static final String PRICESKUFAILED = "This Sku doesn't present in the internal table";
    public static final String PRICEBUILDFAILED = "Product build feild is ZERO";
    public static final String PRICENAMEFAILED = "Product doesn't have name";
    public static final String PIRCEPRODUCTNAMEFAILED = "This product doesn't present in the INTERNAL table";
    public static final String PRICETYPE = "PRICE_UPDATE";
    public static int emptyQueueCount = 0;
    public static int minuteOfExecution = 10;
    public static String currentDateFormat = "dd-MM-yyyyHH-mm-ss";
    public static String productReportFileName = " Product Report ";
    public static String DEVELOPER_MAIL_SUB = "Product Integration Error Mail Alert";
    public static String s3SubjectMail = "Product Integration S3 Bucket Folder";
    
//    public static final String KLEVUPENDING = "KLE_PENDING";
//    public static final String KLEVUUPDATE = "KLE_UPDATE";
//    public static final String KLEVUDELETE = "KLE_DELETE";
//    public static final String KLEVUSUCCESS = "KLE_SUCCESS";
    
    public static final String BCPRODUCT = "BC Product";
    public static final String BCCATEGORY = "BC Category";
    public static final String PRODUCTDELETESCOPE = "store/product/deleted";
}
