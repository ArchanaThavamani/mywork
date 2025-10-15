/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.service;

import com.arizon.ordercommons.configs.OCConstants;
import static com.arizon.ordercommons.configs.OCConstants.secrects;
import com.arizon.ordercommons.util.OrderUtility;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author kavinkumar.s
 */
@Service
@Slf4j
public class OCEmailServices {

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

    public void sendAlertMail(String mailMessage, String sub, String toMailAddress) {
        log.info("Entered into EmailServices.sendAlertMail functionality");
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());
            String content = "Hi ,  <br><br>"
                    + mailMessage + "<br>"
                    + "Thanks " + "<br>"
                    + "Arizon ";

            Optional<Session> optionalSession = getSession();
            if (optionalSession.isPresent()) {
                Message message = new MimeMessage(optionalSession.get());
                message.setFrom(new InternetAddress(secrects.getFromMail(), false));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailAddress));   // need to verify
                message.setSubject(sub + " " + dateValue);
                message.setContent(content, "text/html");
                Transport.send(message);

            }
        } catch (MessagingException e) {
            log.error("Exception on sending Alert Mail : " + OrderUtility.getStackTrace(e));
        }
    }

    public void OrderErrorMail(int orderId, String error, String sub) {

        String Content = "Hi,  <br>"
                + "Order ID: " + orderId + "<br>"
                + "Error  : " + error + "<br>" + "<br>"
                + OCConstants.Thanks + "<br>"
                + OCConstants.Arizon;
        log.info("SSMailService - OrderErrorMail method started");
        try {
            int port = Integer.valueOf(secrects.getPort());
            Properties props = new Properties();
            props.put(OCConstants.smtpAuth, "true");
            props.put(OCConstants.smtpenable, "true");
            props.put(OCConstants.smtpHost, secrects.getHostMail());
            props.put(OCConstants.smtpPort, port);
            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrects.getSMTPuser(), secrects.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(secrects.getFromMail(), false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));
            message.setSubject(sub);
            message.setContent(Content, OCConstants.Content);
            Transport.send(message);
        } catch (Exception e) {
            log.error("MailService.OrderErrorMail :" + OrderUtility.getStackTrace(e));
        }
    }

    public void sendErrorMail(String method, String error, String sub) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Date dateValue = new Date(timestamp.getTime());

            String content = "Hi ,  <br><br>"
                    + "Error in the following method   :<br>"
                    + "Method : " + method + "<br>"
                    + "Error  : " + error + "<br>" + "<br>"
                    + "Thanks " + "<br>"
                    + "Arizon ";

            Optional<Session> optionalSession = getSession();
            if (optionalSession.isPresent()) {
                Message message = new MimeMessage(optionalSession.get());
                message.setFrom(new InternetAddress(secrects.getFromMail(), false));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));   // need to verify
                message.setSubject(sub + " " + dateValue);
                message.setContent(content, "text/html");
                Transport.send(message);

            }
        } catch (MessagingException e) {
            log.error("Exception on sending developer mail alert : " + OrderUtility.getStackTrace(e));

        }
    }


    public void sendWebhookReportMail(String sub, String content) {
        try {
            Optional<Session> optionalSession = getSession();
            if (optionalSession.isPresent()) {
                Message message = new MimeMessage(optionalSession.get());
                message.setFrom(new InternetAddress(secrects.getFromMail(), false));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));   // need to verify
                message.setSubject(sub);
                message.setContent(content, "text/html");
                Transport.send(message);
            }
        } catch (MessagingException e) {
            log.error("Exception on sending mail for sendWebHookReportMail  " + OrderUtility.getStackTrace(e));
        }
    }

    
	public void reportMail(String frontcontent, File file, String fileName, int successCount,String sub) {
		log.info("Report Mail Method Started. ");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date dateValue = new Date(timestamp.getTime());
		String Content = "Hello,  \r\n" + "\r\n" + frontcontent + "\r\n" + "\r\n" + "Here's the summary.\r\n"
				+ "Success : " + successCount + "\r\n"  + "\r\n" + "Thanks, \r\n"
				+ "Arizon.";
		log.info("MailService - Report Mail method started");
		try {
			Optional<Session> optionalSession = getSession();
			if (optionalSession.isPresent()) {
				Message message = new MimeMessage(optionalSession.get());
				message.setFrom(new InternetAddress(secrects.getFromMail(), false));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getClientMailTo()));
				message.setSubject(sub + " " + dateValue);
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(Content);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
				Transport.send(message);
			}
		} catch (MessagingException e) {
			log.error("EXception on Customer-Commons.CustomerServices.ReportMail  " + OrderUtility.getStackTrace(e));
			sendErrorMail("MailService -> ReportMail", OrderUtility.getStackTrace(e),
					secrects.getClientName() + OCConstants.DEVELOPER_MAIL_SUB);
		}
		log.info("Report Mail Method ended.");
	}

    public void sendErrorMailV2(String clientName, String method, String error, String sub, String prefix) {
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
            props.put("mail.smtp.host", secrects.getHostMail());
            props.put("mail.smtp.port", secrects.getPort());
            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(secrects.getSMTPuser(), secrects.getSMTPpass());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(secrects.getFromMail(), false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(secrects.getDeveloperMailTo()));
            message.setSubject(clientName+" : "+ sub + " " + dateValue);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error(OrderUtility.getStackTrace(e));
        }
    }

}
