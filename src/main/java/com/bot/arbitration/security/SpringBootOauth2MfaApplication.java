package com.bot.arbitration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arangodb.springframework.core.ArangoOperations;
import com.bot.arbitration.security.model.User;

@SpringBootApplication
public class SpringBootOauth2MfaApplication implements CommandLineRunner {

	@Autowired
	private ArangoOperations arangoOperations;
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootOauth2MfaApplication.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		arangoOperations.collection(User.class);
		
	}

}
