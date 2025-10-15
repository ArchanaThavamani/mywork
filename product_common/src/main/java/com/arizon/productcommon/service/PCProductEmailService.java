/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.service;

import com.arizon.productcommon.config.PCConstants;
import static com.arizon.productcommon.config.PCConstants.secrets;
import com.arizon.productcommon.util.ProductUtility;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author sasikumar.a
 */
@Service
@Slf4j
public class PCProductEmailService {

    public void sendErrorMail(String clientName, String method, String error, String sub, String prefix) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());

            String content = "Hi ,  <br><br>"
                    + "Error in the following method   :<br>"
                    + "Method : " + method + "<br>"
                    + "Error  : " + error + "<br>" + "<br>"
                    + "Thanks " + "<br>"
                    + "Arizon ";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", secrets.getHostMail());
            props.put("mail.smtp.port", secrets.getPort());
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrets.getSMTPuser(), secrets.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(secrets.getFromMail(), false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrets.getDeveloperMailTo()));
            message.setSubject(clientName+" : "+ sub + " " + dateValue);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error(ProductUtility.getStackTrace(e));
        }
    }

    public void sendReportMail(String clientName, String subject, byte[] reportFile, int success, int failure, String prefix) throws ParseException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date_value = new Date(timestamp.getTime());

        try {
            String content = "Hi,  <br><br>"
                    + "Please find the Product integration report details." + "<br>"
                    + "<br>"
                    + "Here's the summary.<br><br>"
                    + "Success : " + success + "<br>"
                    + "Failed  : " + failure + "<br>"
                    + "<br>"
                    + "Thanks" + "<br>"
                    + "Arizon";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", secrets.getHostMail());
            props.put("mail.smtp.port", secrets.getPort());
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrets.getSMTPuser(), secrets.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper((MimeMessage) message, true);
            helper.setFrom(new InternetAddress(secrets.getFromMail(), false));
            helper.setTo(InternetAddress.parse(secrets.getReportMailTo()));
            helper.setSubject(clientName+" : "+ subject + " " + date_value);
            helper.setText(content, true);
            helper.addAttachment("ReportSummary.xlsx", new ByteArrayResource(reportFile));
            Transport.send(message);
        } catch (MessagingException e) {
            log.error(ProductUtility.getStackTrace(e));
            this.sendErrorMail(secrets.getClientName(), "sendReportMail", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, prefix);
        }
    }

    public void sendApplicationStatusMail(String clientName, String status, String sub, String prefix, String batch) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());

            String content = "Hi ,  <br><br>"
                    + batch+" batch for <b>Product Integration</b> has been " + status + " - '" + dateValue + "'.<br><br>"
                    + "Thanks " + "<br>"
                    + "Arizon ";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", secrets.getHostMail());
            props.put("mail.smtp.port", secrets.getPort());
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrets.getSMTPuser(), secrets.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(secrets.getFromMail(), false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrets.getDeveloperMailTo()));
            message.setSubject(clientName+" : "+ sub + " " + dateValue);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error(ProductUtility.getStackTrace(e));
            this.sendErrorMail(secrets.getClientName(), "sendApplicationStatusMail", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, prefix);
        }
    }
    
    public void sendBucketStatusMail(String fileName, String statusPath, String destinationKey, String client, String subject, String prefix){
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());

            String content = "Hi ,  <br><br>"
                    + "Product Integration s3 bucket folder has been moved to " + statusPath + " / " + fileName + ".<br><br>"
                    + "Thanks " + "<br>"
                    + "Arizon ";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", secrets.getHostMail());
            props.put("mail.smtp.port", secrets.getPort());
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrets.getSMTPuser(), secrets.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(secrets.getFromMail(), false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrets.getDeveloperMailTo()));
            message.setSubject(client+" : "+subject + " " + dateValue);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Method : sendBucketStatusMail "+ ProductUtility.getStackTrace(e));
            this.sendErrorMail(secrets.getClientName(), "sendBucketStatusMail", ProductUtility.getStackTrace(e), PCConstants.DEVELOPER_MAIL_SUB, prefix);
        }
    }

}
