/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.util;

import com.arizon.ordercommons.configs.OCConstants;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author kalaivani.r
 */
@Service
@Slf4j
public class OrderUtility {

    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        if (e != null) {
            e.printStackTrace(pWriter);
        } else {
            pWriter.println("Input is a null exception object");
        }
        return sWriter.toString();
    }

    public static String getCurrentData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(OCConstants.currentDateFormat);
        LocalDateTime now = LocalDateTime.now();
        String curDate = dtf.format(now);
        return curDate;
    }
}
