package dev.sultanov.springboot.oauth2.mfa.config;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.sultanov.springboot.oauth2.mfa.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByUsername(String username);
	User findByVerificationCode(String verificationCode);

}
