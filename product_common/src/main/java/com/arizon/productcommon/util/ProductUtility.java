package com.arizon.productcommon.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductUtility {

   

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

}
