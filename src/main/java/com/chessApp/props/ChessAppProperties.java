package com.chessApp.props;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ChessAppProperties {

	private static final Logger logger = Logger
			.getLogger(ChessAppProperties.class);

	private static Resource resource = new ClassPathResource(
			"/chessApp.properties");

	private static Properties props;

	public static String smtpServer() {
		String smtpServer = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			smtpServer = props.getProperty("smtp.host");
		} catch (IOException e) {
			logger.debug(e);
		}
		return smtpServer;
	}
	
	public static String appMail() {
		String appMail = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			appMail = props.getProperty("app.mail");
		} catch (IOException e) {
			logger.debug(e);
		}
		return appMail;
	}
	
	public static String dbName() {
		String dbName = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			dbName = props.getProperty("db.name");
		} catch (IOException e) {
			logger.debug(e);
		}
		return dbName;
	}
	
	public static String appContextpath() {
		String appContextpath = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			appContextpath = props.getProperty("app.contextpath");
		} catch (IOException e) {
			logger.debug(e);
		}
		return appContextpath;
	}
	
	public static String domainName() {
		String domainName = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
			domainName = props.getProperty("domain.name");
		} catch (IOException e) {
			logger.debug(e);
		}
		return domainName;
	}
	
	// FOR TEST
	public static void main(String[] args) {
		System.out.println(ChessAppProperties.smtpServer());
		System.out.println(ChessAppProperties.appMail());
		System.out.println(ChessAppProperties.dbName());
		System.out.println(ChessAppProperties.appContextpath());
		System.out.println(ChessAppProperties.domainName());
	}

}
