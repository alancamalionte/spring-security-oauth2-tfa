package dev.sultanov.springboot.oauth2.mfa.config;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.sultanov.springboot.oauth2.mfa.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	Usuario findByUsername(String username);
	Usuario findByVerificationCode(String verificationCode);

}
