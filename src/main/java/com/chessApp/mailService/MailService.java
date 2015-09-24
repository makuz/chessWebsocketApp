package com.chessApp.mailService;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.chessApp.props.ChessAppProperties;

@Service
public class MailService {

	private final String HOST = ChessAppProperties.smtpServer();

	private final Logger logger = Logger.getLogger(MailService.class);

	private final Properties properties = System.getProperties();

	private final Session session = Session.getDefaultInstance(properties);

	private final String HTML_MIME_TYPE = "text/html";

	private JavaMailSenderImpl javaMailSender;

	public void sendMail(String to) {
		logger.debug("sendMail()");

		properties.setProperty("mail.smtp.host", HOST);

		try {

			javaMailSender = new JavaMailSenderImpl();
			javaMailSender.setHost(HOST);

			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(MailTematPL.REJESTRACJA);
			message.setContent("<h1>This is gggggggggggggg</h1>",
					HTML_MIME_TYPE);

			Transport.send(message);

			logger.debug("send email to " + to);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	// FOR TESTS ---------------------------
	public static void main(String[] args) {

		MailService ms = new MailService();
		ms.sendMail("marcin.kuzdowicz@wp.pl");
	}

}
