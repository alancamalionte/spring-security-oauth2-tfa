package dev.sultanov.springboot.oauth2.mfa.config;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	Usuario findByUsername(String username);

}
