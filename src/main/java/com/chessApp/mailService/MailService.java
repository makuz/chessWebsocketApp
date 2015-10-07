package com.chessApp.mailService;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.chessApp.props.ChessAppProperties;

@Service
public class MailService {

	private final Logger logger = Logger.getLogger(MailService.class);

	private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	private static Resource resource = new ClassPathResource(
			"/chessApp.properties");

	private void prepareMailSender() {

		Properties properties = new Properties();
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.debug(e);
		}

		mailSender.setUsername(properties.getProperty("username"));
		mailSender.setPassword(properties.getProperty("password"));
		mailSender.setHost(properties.getProperty("smtp.host"));
		mailSender.setPort(Integer.parseInt(properties.getProperty("port")));
		mailSender.setProtocol(properties
				.getProperty("mail.transport.protocol"));
		mailSender.setJavaMailProperties(properties);
	}

	private String prepareRegistrationMailText(String randomHashForLink) {

		StringBuilder sb = new StringBuilder();
		String siteName = ChessAppProperties.getProperty("domain.name");
		sb.append("<html><head><meta http-equiv=\"Content-Type\" ");
		sb.append("content=\"text/html; charset=UTF-8\"></head><body>");
		sb.append("<p>");
		sb.append("<br />");
		sb.append("Witaj,");
		sb.append("<br />");
		sb.append("Dziękujemy za rejestrację w serwisie <b>" + siteName
				+ "</b>");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("<b>Potwierdź swój adres e-mail</b>, aby dokończyć proces rejestracji. ");
		sb.append("<br />");
		sb.append(prepareRegistrationAnchor(randomHashForLink));
		sb.append("<br />");
		sb.append("Konto należy potwierdzić w przeciągu tygodnia, w przeciwnym razie dane zostaną usunięte.");
		sb.append("<br />");
		sb.append("Link aktywacyjny również straci swoją ważność po tygodniu.");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("Pozdrawiamy,");
		sb.append("<br />");
		sb.append("Zespół <b>" + siteName + "</b>");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("<br />");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("Wiadomość została wysłana automatycznie z serwisu ");
		sb.append(siteName);
		sb.append("Jeżeli nie rejestrowałeś się w serwisie, poprostu zignoruj tę wiadomość. ");
		sb.append("</p>");
		sb.append("</body></html>");

		return sb.toString();
	}

	private String prepareRegistrationAnchor(String hash) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"");
		String domainName = ChessAppProperties.getProperty("domain.link");
		String confirmEmailLink = domainName + "/registration/confirm/" + hash;
		sb.append(confirmEmailLink);
		sb.append("\">");
		sb.append("Kliknij tutaj, aby potwierdzić adres email");
		sb.append("</a>");
		System.out.println(sb.toString());
		return sb.toString();
	}

	public void sendMail(String to, String from, String subject,
			String messageContent) {
		logger.debug("sendMail()");

		prepareMailSender();

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
				mimeMessage, "UTF-8");

		try {
			mimeMessageHelper.setTo(new InternetAddress(to));
			mimeMessageHelper.setFrom(new InternetAddress(from));
			mimeMessageHelper.setSubject(subject);
			mimeMessage.setContent(messageContent, "text/html; charset=utf-8");

		} catch (MessagingException | MailAuthenticationException e) {
			logger.debug(e);
		} 

		mailSender.send(mimeMessage);
		logger.debug("send email to " + to);

	}

	public void sendRegistrationMail(String to, String randomHashForLink) {

		String from = ChessAppProperties
				.getProperty("mail.default.message.from");

		sendMail(to, from, MailSubjectPL.REJESTRACJA,
				prepareRegistrationMailText(randomHashForLink));

	}

}
