package com.arizon.Guidant.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;


public class SAPDateUtils {

	public static String toSAPDateFormat(LocalDateTime dateTime) {
        long millis = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        return "/Date(" + millis + ")/";
    }
	
	public static LocalDateTime toLocalDateTime(String date) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	     LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
	 	 return dateTime;
	}
	
	public static String convertBCDateFormatToSAP(String dateTime) {
		LocalDateTime localDateTime = toLocalDateTime(dateTime);
		return toSAPDateFormat(localDateTime);
	}
	
	public static String yesterdayDate() {
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return yesterday.format(formatter);
	}
	
	public static String sanitizeDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        // Remove any non-numeric characters
        String sanitizedDate = date.replaceAll("[^0-9]", "");
        // Ensure the date is in the format YYYYMMDD HHMMSS
        if (sanitizedDate.length() >= 14) {
            return sanitizedDate.substring(0, 14);
        } else {
            return sanitizedDate; // Return as is if not enough characters
        }
    }
}
