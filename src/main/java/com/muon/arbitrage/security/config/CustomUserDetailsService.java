package com.muon.arbitrage.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.muon.arbitrage.security.model.User;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsCustom(usuario.getUsername(), usuario.getPassword(), usuario.isActive(), true, true, true, usuario.getAuthoritys(), usuario.getTokenCredentials(), usuario.getId());
//        return new UserDetailsCustom(usuario.getUsername(), usuario.getPassword(), usuario.getAuthoritys(), usuario.getGoogleAuthCredentials());
    }
    
}
