package com.muon.arbitrage.security.config;

import java.util.Optional;

import com.arangodb.springframework.repository.ArangoRepository;
import com.muon.arbitrage.security.model.User;

public interface UserRepository extends ArangoRepository<User, String> {

	Optional<User> findByUsername(String username);
	Optional<User> findByVerificationCode(String verificationCode);

}
