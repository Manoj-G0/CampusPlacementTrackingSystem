package com.cpt.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.cpt.dao.MailDAOInt;
import com.cpt.model.EmailLog;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
@PropertySource("classpath:db.properties")
public class MailService {
	@Value("${USER}")
	private String fromEmail;
	@Value("${PASSWORD}")
	private String password;

	private final MailDAOInt mailDao;

	public MailService(MailDAOInt mailDao) {
		this.mailDao = mailDao;
		mailDao.initTable();
	}

	// To send the email
	public boolean sendEmail(String from, String to, String subject, String messageContent,
			HashMap<String, String> content) {
		EmailLog emailLog = new EmailLog();
		emailLog.setSender(from);
		emailLog.setRecipient(to);
		emailLog.setSubject(subject);
		emailLog.setSentAt(LocalDateTime.now());

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
			@Override
			protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
				return new jakarta.mail.PasswordAuthentication(fromEmail, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			// Set HTML content with dynamic replacements
			String finalContent = messageContent;
			for (String key : content.keySet()) {
				finalContent = finalContent.replace("{" + key + "}", content.get(key));
			}
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(finalContent, "text/html");

			MimeMultipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			Transport.send(message);
			emailLog.setStatus("SUCCESS");
			// LOGGER.info("Email sent successfully to " + to);
		} catch (MessagingException e) {
			emailLog.setStatus("FAILED");
			// LOGGER.severe("Failed to send email to " + to + ": " + e.getMessage());
			mailDao.saveEmailLog(emailLog);
			return false;
		}
		mailDao.saveEmailLog(emailLog);
		return true;
	}
}