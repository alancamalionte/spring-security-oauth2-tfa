package dev.sultanov.springboot.oauth2.mfa.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import dev.sultanov.springboot.oauth2.mfa.config.UserRepository;
import dev.sultanov.springboot.oauth2.mfa.exception.ApiException;
import dev.sultanov.springboot.oauth2.mfa.model.User;
import dev.sultanov.springboot.oauth2.mfa.model.UserDto;
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
		User user = usuarioRepository.findByVerificationCode(verificationCode);
		user.setActive(true);
		usuarioRepository.save(user);
	}

	@PutMapping("/{id}")
	public void update(@RequestBody UserDto userDto, @PathVariable String id) {
		User user = usuarioRepository.findById(id).orElseThrow(() ->new ApiException(HttpStatus.BAD_REQUEST, 2, "Usuário não encontrado"));
		usuarioRepository.save(user);
	}

	@PostMapping("/tfa/{token}")
	public String activeGoogleAuthenticator(@PathVariable String token) {
		User usuario = usuarioRepository.findByVerificationCode(token);
		final GoogleAuthenticatorConfigBuilder gacb =
				new GoogleAuthenticatorConfigBuilder()
				.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
				.setWindowSize(5)
				.setNumberOfScratchCodes(10);

		final GoogleAuthenticator gAuth = new GoogleAuthenticator(gacb.build());
		final GoogleAuthenticatorKey credentials = gAuth.createCredentials();
		return GoogleAuthenticatorQRGenerator.getOtpAuthURL("BitBotBox", usuario.getName(), credentials);
	}

	public static void main(String[] args) {
		GoogleAuthenticatorConfigBuilder gacb =
				new GoogleAuthenticatorConfigBuilder()
				.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(30))
				.setWindowSize(5)
				.setNumberOfScratchCodes(10);

		GoogleAuthenticator gAuth = new GoogleAuthenticator(gacb.build());
		//		
		final GoogleAuthenticatorKey credentials = gAuth.createCredentials();
		System.out.println(credentials.getKey());
		System.out.println(credentials.getVerificationCode());
		System.out.println(credentials.getScratchCodes());


		//		System.out.println(gAuth.authorize("LKHXM2VYFC7EJRZP", 884136));
		//		System.out.println(gAuth.authorize("JZ2YFY75HWQQ32CW", 235595));
		//		System.out.println(gAuth.authorize("JZ2YFY75HWQQ32CW", 716567));
		//		System.out.println(gAuth.authorize("JZ2YFY75HWQQ32CW", 494924));


		String QRCode = GoogleAuthenticatorQRGenerator.getOtpAuthURL("sadsad", "alan", credentials);
		System.out.println(QRCode);


	}

}
//JZ2YFY75HWQQ32CW
//887606
//[98618758, 86207758, 97929790, 36894139, 49264211, 63039633, 26130229, 32314932, 37509234, 34950616]
//false
//false
//https://chart.googleapis.com/chart?chs=200x200&chld=M%7C0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fsadsad%3Aalan%3Fsecret%3DJZ2YFY75HWQQ32CW%26issuer%3Dsadsad%26algorithm%3DSHA1%26digits%3D6%26period%3D30

