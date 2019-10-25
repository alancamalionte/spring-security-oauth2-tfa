package com.box.arbitration.config;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.box.arbitration.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);
	Optional<User> findByVerificationCode(String verificationCode);

}
