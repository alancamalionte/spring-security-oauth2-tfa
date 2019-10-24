package dev.sultanov.springboot.oauth2.mfa.service;

import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import dev.sultanov.springboot.oauth2.mfa.config.UserDetailsCustom;

@Service
public class MfaService {

    private GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();


    public boolean verifyCode(UserDetailsCustom userDetails, int code) {
    	
    	// TODO implementar kms
        return googleAuthenticator.authorize(userDetails.getGoogleCredentials().getKey(), code);
    }
}
