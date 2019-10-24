package dev.sultanov.springboot.oauth2.mfa.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import dev.sultanov.springboot.oauth2.mfa.model.GoogleCredentials;
import lombok.Getter;

@Getter
public class UserDetailsCustom extends User {
	
	private static final long serialVersionUID = 3754383572899448304L;
	
	private GoogleCredentials googleCredentials;

	public UserDetailsCustom(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, GoogleCredentials googleCredentials) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.googleCredentials = googleCredentials;
	}

	public UserDetailsCustom(String username, String password, List<GrantedAuthority> authoritys, GoogleCredentials googleCredentials) {
		super(username, password, authoritys);
		this.googleCredentials = googleCredentials;
	}

}
