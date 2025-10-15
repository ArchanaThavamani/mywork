package com.arizon.racetrac_salesorder.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.stereotype.Service;

@Service
public class RacetracUtil {
	public RacetracUtil () {
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
