package com.chessApp.confs;

//import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.chessApp.props.ChessAppProperties;
import com.mongodb.*;

@Configuration
@EnableMongoRepositories("com.chessApp.daoimpl")
public class MongoDBConnectionConfig extends AbstractMongoConfiguration {

	ChessAppProperties chessappProperties = new ChessAppProperties();

	String username = "makuzadmin";
	String password = "1MojeFajneAuto";
	String host = "mongodb22592-iboard-games.unicloud.pl";
	String dbname = "chessapp_db";
	String mongoLabConnectionString = ChessAppProperties
			.getProperty("db.connectionstring");

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return dbname;
	}

	@Override
	public Mongo mongo() throws Exception {

		// inny rodzaj konfiguracji
		// MongoCredential credential =
		// MongoCredential.createScramSha1Credential(
		// username, dbname, password.toCharArray());

		MongoClientURI uri = new MongoClientURI(mongoLabConnectionString);

		// inny rodzaj konfiguracji
		// return new MongoClient(new ServerAddress(host),
		// Arrays.asList(credential));

		return new MongoClient(uri);
	}

}
