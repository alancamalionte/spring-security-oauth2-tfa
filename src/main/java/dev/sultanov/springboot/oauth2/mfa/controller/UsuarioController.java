package dev.sultanov.springboot.oauth2.mfa.controller;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import dev.sultanov.springboot.oauth2.mfa.config.UsuarioRepository;
import dev.sultanov.springboot.oauth2.mfa.model.Usuario;

@RestController
@RequestMapping("v1/user")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping
	public void criarUsuario(@Valid @RequestBody Usuario usuario) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuario.setAuthoritys(AuthorityUtils.createAuthorityList("ROLE_USER"));
		usuario.setGoogleAuthGenerated(false);
		usuario.setActive(false);
		usuario.setVerificationCode(UUID.randomUUID().toString());
		usuarioRepository.save(usuario);
		//TODO enviar email de ativação
	}


	@PostMapping("/tfa/{token}")
	public String activeGoogleAuthenticator(@PathVariable String token) {
		Usuario usuario = usuarioRepository.findByVerificationCode(token);
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

