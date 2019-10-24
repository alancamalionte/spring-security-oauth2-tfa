package dev.sultanov.springboot.oauth2.mfa.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import dev.sultanov.springboot.oauth2.mfa.model.Usuario;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
    private UsuarioRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsCustom(usuario.getUsername(), usuario.getPassword(), usuario.getAuthoritys(), "LKHXM2VYFC7EJRZP");
    }
    
}
