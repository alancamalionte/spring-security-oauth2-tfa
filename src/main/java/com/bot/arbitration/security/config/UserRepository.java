package com.bot.arbitration.security.config;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bot.arbitration.security.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);
	Optional<User> findByVerificationCode(String verificationCode);

}
