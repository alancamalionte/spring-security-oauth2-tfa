package dev.sultanov.springboot.oauth2.mfa.model;

import java.util.List;

import lombok.Data;

@Data
public class GoogleCredentials {

	private List<Integer> scratchCode;
	private String key;
}
