package dev.sultanov.springboot.oauth2.mfa.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import dev.sultanov.springboot.oauth2.mfa.config.granter.MfaTokenGranter;
import dev.sultanov.springboot.oauth2.mfa.config.granter.PasswordTokenGranter;
import dev.sultanov.springboot.oauth2.mfa.service.MfaService;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private AuthenticationManager authenticationManager;
    private MfaService mfaService;

    @Autowired
    public AuthServerConfig(AuthenticationManager authenticationManager,
                            MfaService mfaService) {
        this.authenticationManager = authenticationManager;
        this.mfaService = mfaService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenGranter(tokenGranter(endpoints));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("secret"))
                .authorizedGrantTypes("password", "mfa")
                .scopes("read");
    }

    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        granters.add(new PasswordTokenGranter(endpoints, authenticationManager, mfaService));
        granters.add(new MfaTokenGranter(endpoints, authenticationManager, mfaService));
        return new CompositeTokenGranter(granters);
    }
}
