package com.muon.arbitrage.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.muon.arbitrage.security.config.granter.MfaTokenGranter;
import com.muon.arbitrage.security.config.granter.PasswordTokenGranter;
import com.muon.arbitrage.security.service.MfaService;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private MfaService mfaService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private CustomAccessTokenConverter customAccessTokenConverter;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.checkTokenAccess("isAuthenticated()");
//		security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("client")
		.secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("secret"))
		.authorizedGrantTypes("password", "mfa")
		.scopes("read")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(7200);
	}


	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		endpoints.tokenStore(tokenStore())
		.tokenEnhancer(tokenEnhancerChain)
		.tokenGranter(tokenGranter(endpoints));
	}    
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(customAccessTokenConverter);
		converter.setSigningKey("123");
		return converter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}    

	private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
		List<TokenGranter> granters = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
		granters.add(new PasswordTokenGranter(endpoints, authenticationManager, userRepository));
		granters.add(new MfaTokenGranter(endpoints, authenticationManager, mfaService, customUserDetailsService));
		return new CompositeTokenGranter(granters);
	}
}
