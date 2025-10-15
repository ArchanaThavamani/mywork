/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.service;

import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.util.OrderUtility;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Service
@Slf4j
public class OCBucketMailServices {

    private Optional<Session> getSession() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", OCConstants.secrects.getHostMail());
            props.put("mail.smtp.port", secrects.getPort());
            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrects.getSMTPuser(), secrects.getSMTPpass());
                }
            });

            return Optional.of(session);

        } catch (Exception e) {
            log.error("Exception on getting SMTP session  : " + OrderUtility.getStackTrace(e));
            return Optional.empty();
        }
    }

    public String sendBucketStatusMail(String fileName, String status, String destinationKey, String sub) {
        log.info("Entered into BucketMailServices.sendBucketStatusMail method ");
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());
            String content = "Hi,  <br><br>"
                    + "Please find the status of moving customer data from S3 bucket below : <br>"
                    + "File :" + fileName + "<br>"
                    + "Destination Folder : " + destinationKey + "<br>"
                    + "Status : " + status + "<br>"
                    + OCConstants.Thanks + "<br>"
                    + OCConstants.Arizon;

            Optional<Session> optionalSession = getSession();
            if (optionalSession.isPresent()) {
                Message message = new MimeMessage(optionalSession.get());
                message.setFrom(new InternetAddress(secrects.getFromMail(), false));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));
                message.setSubject(sub + " " + dateValue);
                message.setContent(content, "text/html");
                Transport.send(message);
            }
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on BucketMailServices.sendBucketStatusMail method : " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }

    public String sendBucketErrorsMail(String method, String error, String sub) {
        log.info("Entered into BucketMailServices.sendBucketStatusMail method ");
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());
            String content = "Hi,  <br><br>"
                    + "Error in the following method   :<br>"
                    + "Method :" + method + "<br>"
                    + "Error  :" + error + "<br>" + "<br>"
                    + OCConstants.Thanks + "<br>"
                    + OCConstants.Arizon;

            Optional<Session> optionalSession = getSession();
            if (optionalSession.isPresent()) {
                Message message = new MimeMessage(optionalSession.get());
                message.setFrom(new InternetAddress(secrects.getFromMail(), false));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));
                message.setSubject(sub + "" + dateValue);
                message.setContent(content, "text/html");
                Transport.send(message);
            }
            return OCConstants.success;
        } catch (Exception e) {
            log.error("Exception on BucketMailServices.sendBucketStatusMail method : " + OrderUtility.getStackTrace(e));
            return OCConstants.failed;
        }
    }
}
