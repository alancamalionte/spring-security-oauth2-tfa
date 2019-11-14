package com.bot.arbitration.security.config;

import org.springframework.context.annotation.Configuration;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDB.Builder;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;

@Configuration
@EnableArangoRepositories(basePackages = { "com.bot.arbitration.security" })
public class ArangoConfig implements ArangoConfiguration {
	
	@Override
	public Builder arango() {
		return new ArangoDB.Builder().host("localhost", 8529).user("root").password("root");
	}

	@Override
	public String database() {
		return "spring-demo";
	}
}
