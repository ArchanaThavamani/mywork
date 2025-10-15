package com.arizon.racetrac.util;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class RacetracUtil {
    public RacetracUtil(){     
    
    }

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
