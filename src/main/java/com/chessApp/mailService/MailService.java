package com.chessApp.mailService;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.chessApp.props.ChessAppProperties;

@Service
public class MailService {

	private final Logger logger = Logger.getLogger(MailService.class);

	private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	private void prepareMailSender() {
		mailSender.setUsername(ChessAppProperties.getProperty("username"));
		mailSender.setPassword(ChessAppProperties.getProperty("password"));
		mailSender.setHost(ChessAppProperties.getProperty("smtp.host"));
		// mailSender.setPort(Integer.parseInt(ChessAppProperties.getProperty("port")));
		mailSender.setProtocol(ChessAppProperties
				.getProperty("mail.transport.protocol"));
	}

	private String prepareMailText() {

		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><meta http-equiv=\"Content-Type\" ");
		sb.append("content=\"text/html; charset=UTF-8\"></head><body>");
		sb.append("<p>");
		sb.append("<br />");
		sb.append("Witaj,");
		sb.append("<br />");
		sb.append("Dziękujemy za rejestrację w serwisie <b>"
				+ ChessAppProperties.getProperty("domain.name") + "</b>");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("<b>Potwierdź swój adres e-mail</b>, aby dokończyć proces rejestracji. ");
		sb.append("<br />");
		sb.append(prepareRegistrationAnchor());
		sb.append("<br />");
		sb.append("<br />");
		sb.append("Pozdrawiamy,");
		sb.append("<br />");
		sb.append("Zespół <b>" + ChessAppProperties.getProperty("domain.name")
				+ "</b>");
		sb.append("<br />");
		sb.append("</p>");
		sb.append("</body></html>");

		return sb.toString();
	}

	private String prepareRegistrationAnchor() {
		StringBuilder sb = new StringBuilder();
		sb.append("<a target=\"_blank\" href=\"http://www.google.pl/\">");
		sb.append("Kliknij tutaj, aby potwierdzić adres email");
		sb.append("</a>");
		return sb.toString();
	}

	public void sendMail(String to) {
		logger.debug("sendMail()");

		prepareMailSender();

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
				mimeMessage, "UTF-8");

		try {
			mimeMessageHelper.setTo(new InternetAddress(to));
			mimeMessageHelper.setFrom(new InternetAddress(ChessAppProperties
					.getProperty("mail.default.message.from")));
			mimeMessageHelper.setSubject(MailSubjectPL.REJESTRACJA);
			mimeMessage.setContent(prepareMailText(),
					"text/html; charset=utf-8");

		} catch (AddressException e) {
			logger.debug(e);
		} catch (MessagingException e) {
			logger.debug(e);
		}

		mailSender.send(mimeMessage);
		logger.debug("send email to " + to);

	}

	// FOR TESTS ---------------------------
	public static void main(String[] args) {

		MailService ms = new MailService();
		ms.sendMail("marcin.kuzdowicz@wp.pl");
	}

}
