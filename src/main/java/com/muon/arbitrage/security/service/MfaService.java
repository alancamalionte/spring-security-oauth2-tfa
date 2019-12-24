package com.muon.arbitrage.security.service;

import org.springframework.stereotype.Service;

import com.muon.arbitrage.security.config.UserDetailsCustom;
import com.warrenstrange.googleauth.GoogleAuthenticator;

@Service
public class MfaService {

    private GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();


    public boolean verifyCode(UserDetailsCustom userDetails, int code) {
    	// TODO implementar kms
        return googleAuthenticator.authorize(userDetails.getGoogleCredentials().getKey(), code);
    }
}
