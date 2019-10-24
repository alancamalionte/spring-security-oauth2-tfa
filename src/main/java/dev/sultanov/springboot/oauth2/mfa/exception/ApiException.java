package dev.sultanov.springboot.oauth2.mfa.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 8063627714364254403L;

	private HttpStatus statusCode;
	private transient ErrorResponse response;

	public ApiException() {
	}

	public ApiException(HttpStatus statusCode, Integer code, String description) {
		this(statusCode, code, description, null);
	}

	public ApiException(HttpStatus statusCode, Integer code, Integer accessCode, String description) {
		this(statusCode, code, description, null);
	}

	public ApiException(HttpStatus statusCode, Integer code, String description, Throwable cause) {
		super("Gateway: " + code + " - " + description, cause);
		this.statusCode = statusCode;
		String sCause = cause == null ? "" : cause.getMessage();
		response = new ErrorResponse(code, description, sCause);
	}

	public ErrorResponse getResponse() {
		return response;
	}

	public void setResponse(ErrorResponse response) {
		this.response = response;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}
}
