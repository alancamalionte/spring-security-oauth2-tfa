package dev.sultanov.springboot.oauth2.mfa.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import dev.sultanov.springboot.oauth2.mfa.model.User;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        User usuario = Optional.ofNullable(userRepository.findByUsername(authentication.getName())).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("id", usuario.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
