package dev.sultanov.springboot.oauth2.mfa.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MfaService {

    private static final Map<String, String> SECRET_BY_USERNAME = new HashMap<>();
    private GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    static {
    	SECRET_BY_USERNAME.put("teste", "LKHXM2VYFC7EJRZP");
    }
    public boolean isEnabled(String username) {
        return SECRET_BY_USERNAME.containsKey(username);
    }

    public boolean verifyCode(String username, int code) {
        return googleAuthenticator.authorize("LKHXM2VYFC7EJRZP", code);
    }
}
