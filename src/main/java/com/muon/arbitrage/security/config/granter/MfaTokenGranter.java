package com.muon.arbitrage.security.config.granter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.muon.arbitrage.security.config.CustomUserDetailsService;
import com.muon.arbitrage.security.config.UserDetailsCustom;
import com.muon.arbitrage.security.service.MfaService;

public class MfaTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "mfa";

    private final TokenStore tokenStore;
    private final MfaService mfaService;
	private final CustomUserDetailsService customUserDetailsService;
   
	public MfaTokenGranter(AuthorizationServerEndpointsConfigurer endpointsConfigurer, AuthenticationManager authenticationManager, MfaService mfaService, CustomUserDetailsService customUserDetailsService) {
        super(endpointsConfigurer.getTokenServices(), endpointsConfigurer.getClientDetailsService(), endpointsConfigurer.getOAuth2RequestFactory(), GRANT_TYPE);
        this.tokenStore = endpointsConfigurer.getTokenStore();
        this.mfaService = mfaService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        final String mfaToken = parameters.get("mfa_token");
        if (mfaToken != null) {
            OAuth2Authentication authentication = loadAuthentication(mfaToken);
            if (parameters.containsKey("mfa_code")) {
            	UserDetailsCustom userDetails = (UserDetailsCustom) customUserDetailsService.loadUserByUsername(authentication.getUserAuthentication().getName());
                int code = parseCode(parameters.get("mfa_code"));
                if (mfaService.verifyCode(userDetails, code)) {
                    return getAuthentication(tokenRequest, authentication, userDetails);
                }
            } else {
                throw new InvalidRequestException("missing-2FA-code");
            }
            throw new InvalidGrantException("invalid-authentication-code");
        } else {
            throw new InvalidRequestException("missing-2FA-token");
        }
    }

    private OAuth2Authentication loadAuthentication(String accessTokenValue) {
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(accessTokenValue);
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            this.tokenStore.removeAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        } else {
            OAuth2Authentication result = this.tokenStore.readAuthentication(accessToken);
            if (result == null) {
                throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
            }
            return result;
        }
    }

    private int parseCode(String codeString) {
        try {
            return Integer.parseInt(codeString);
        } catch (NumberFormatException e) {
            throw new InvalidGrantException("invalid-authentication-code");
        }
    }


	
    private OAuth2Authentication getAuthentication(TokenRequest tokenRequest, OAuth2Authentication authentication, UserDetails loadUserByUsername) {
		Collection<? extends GrantedAuthority> authorities = loadUserByUsername.getAuthorities();
		Authentication userAuthentication = new UsernamePasswordAuthenticationToken(loadUserByUsername, null, authorities);
		authentication = new OAuth2Authentication(authentication.getOAuth2Request(), userAuthentication);
		return refreshAuthentication(authentication, tokenRequest);
    }

    private OAuth2Authentication refreshAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope + ".", originalScope);
            }

            clientAuth = clientAuth.narrowScope(scope);
        }
        return new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
    }
}
