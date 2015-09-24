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

		} catch (AddressException e) {
			logger.debug(e);
		} catch (MessagingException e) {
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
