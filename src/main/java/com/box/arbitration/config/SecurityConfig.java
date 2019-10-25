package com.box.arbitration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailService;
	
	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//		        .antMatchers("/v1/user/**").permitAll()
//                .anyRequest()
//                .authenticated();
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/v1/user").permitAll()
		.antMatchers(HttpMethod.PATCH, "/v1/user/activate/**").permitAll()
		.anyRequest()
		.authenticated()
		.and().cors().disable().csrf().disable();    	
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
