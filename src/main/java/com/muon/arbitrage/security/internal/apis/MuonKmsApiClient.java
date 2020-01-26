package com.muon.arbitrage.security.internal.apis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.muon.arbitrage.security.model.GoogleCredentials;

@FeignClient(name="muonkms", url="${muon-kms}")
public interface MuonKmsApiClient {

	@PostMapping("v1/crendentials-encryption/encrypt/{idUser}")
	String encrypt(@RequestBody GoogleCredentials googleCredentials, @PathVariable("idUser") String idUser);
	
	@GetMapping("v1/crendentials-encryption/decrypt/{idUser}/{token}")
	GoogleCredentials decrypt(@PathVariable("idUser") String idUser, @PathVariable("token") String token);

}
