package com.box.arbitration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.sultanov.springboot.oauth2.mfa")
public class SpringBootOauth2MfaApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootOauth2MfaApplication.class, args);
    }

}
