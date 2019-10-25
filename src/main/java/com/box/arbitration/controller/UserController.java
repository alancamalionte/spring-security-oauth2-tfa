package com.box.arbitration.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.box.arbitration.config.UserRepository;
import com.box.arbitration.exception.ApiException;
import com.box.arbitration.model.GoogleCredentials;
import com.box.arbitration.model.User;
import com.box.arbitration.model.UserDto;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/user")
@Slf4j
public class UserController {

	@Autowired
	private UserRepository usuarioRepository;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void create(@RequestBody UserDto userDto) {
		if(userDto.getPassword().equals(userDto.getPasswordConfirm())) {
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			User user = User.builder()
					.username(userDto.getUsername())
					.name(userDto.getName())
					.password(passwordEncoder.encode(userDto.getPassword()))
					.celular(userDto.getCelular())
					.authoritys(AuthorityUtils.createAuthorityList("ROLE_USER"))
					.verificationCode(UUID.randomUUID().toString())
					.build();
			usuarioRepository.save(user);
			log.info(user.getVerificationCode());
			//TODO enviar email de ativação
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, 1, "Senha não confere");
		}
	}

	@PatchMapping("/activate/{verificationCode}")
	public void activate(@PathVariable String verificationCode) {
		User user = usuarioRepository.findByVerificationCode(verificationCode).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, 6, "Verification code not found"));
		user.setActive(true);
		usuarioRepository.save(user);
	}

	@PutMapping("/{id}")
	public void update(@RequestBody UserDto userDto, @PathVariable String id) {
		User user = usuarioRepository.findById(id).orElseThrow(() ->new ApiException(HttpStatus.NOT_FOUND, 2, "Usuário não encontrado"));
		usuarioRepository.save(user);
	}

	@PutMapping("/{id}/change-tfa/{enable}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String activeGoogleAuthenticator(@PathVariable String id, @PathVariable boolean enable) {
		User user = usuarioRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, 3, "Usuário não encontrado"));
		final GoogleAuthenticatorConfigBuilder gacb =
				new GoogleAuthenticatorConfigBuilder()
				.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
				.setWindowSize(5)
				.setNumberOfScratchCodes(10);
		final GoogleAuthenticator gAuth = new GoogleAuthenticator(gacb.build());
		final GoogleAuthenticatorKey credentials = gAuth.createCredentials();
		user.setGoogleAuthCredentials(new GoogleCredentials(credentials.getScratchCodes(), credentials.getKey()));
		user.setGoogleAuthEnable(true);
		usuarioRepository.save(user);
		return GoogleAuthenticatorQRGenerator.getOtpAuthURL("BitBotBox", user.getName(), credentials);
	}


}

