package dev.sultanov.springboot.oauth2.mfa.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCredentials {

	private List<Integer> scratchCode;
	private String key;
}
