package dev.sultanov.springboot.oauth2.mfa.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

	private Integer code;

	private String description;

	private final String cause;

}