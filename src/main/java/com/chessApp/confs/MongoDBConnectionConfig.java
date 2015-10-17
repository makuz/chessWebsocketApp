package com.chessApp.confs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.chessApp.props.ChessAppProperties;
import com.mongodb.*;

@Configuration
@EnableMongoRepositories("com.chessApp.daoimpl")
public class MongoDBConnectionConfig extends AbstractMongoConfiguration {

	@SuppressWarnings("unused")
	private ChessAppProperties chessappProperties = new ChessAppProperties();
	private String dbname = "chessapp_db";
	private String mongoLabConnectionString = ChessAppProperties
			.getProperty("db.conn.localhost");

	@Override
	protected String getDatabaseName() {

		return dbname;
	}

	@Override
	public Mongo mongo() throws Exception {

		MongoClientURI uri = new MongoClientURI(mongoLabConnectionString);

		return new MongoClient(uri);
	}

}
