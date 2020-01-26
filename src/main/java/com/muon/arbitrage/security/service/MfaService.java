package com.muon.arbitrage.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muon.arbitrage.security.config.UserDetailsCustom;
import com.muon.arbitrage.security.internal.apis.MuonKmsApiClient;
import com.muon.arbitrage.security.model.GoogleCredentials;
import com.warrenstrange.googleauth.GoogleAuthenticator;

@Service
public class MfaService {

    private GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

	@Autowired
	private MuonKmsApiClient muonKms;
	/*
	 * public boolean verifyCode(UserDetailsCustom userDetails, int code) { // TODO
	 * implementar kms return
	 * googleAuthenticator.authorize(userDetails.getGoogleCredentials().getKey(),
	 * code); }
	 */
	public boolean verifyCode(final UserDetailsCustom user, final int code) {
		GoogleCredentials googleCredentials = decrypt(user.getUserId(), user.getTokenCredentials());
		return googleAuthenticator.authorize(googleCredentials.getKey(), code);
	}

	public String encryptCredentials (GoogleCredentials googleCredentials, String idUser) {
		return muonKms.encrypt(googleCredentials, idUser);
	}
	
	private GoogleCredentials decrypt(String idUser, String tokenCredentials) {
		return muonKms.decrypt(idUser, tokenCredentials);
	}
}
