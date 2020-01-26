package com.muon.arbitrage.security.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserDetailsCustom extends User {
	
	private static final long serialVersionUID = 3754383572899448304L;
	
	private String tokenCredentials;
	private String userId;

	public UserDetailsCustom(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, String tokenCredentials, String userId) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.tokenCredentials = tokenCredentials;
		this.userId = userId;
	}

	public UserDetailsCustom(String username, String password, List<GrantedAuthority> authoritys, String tokenCredentials, String userId) {
		super(username, password, authoritys);
		this.tokenCredentials = tokenCredentials;
		this.userId = userId;
	}

}
