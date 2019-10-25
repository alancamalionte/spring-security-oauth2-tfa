package com.box.arbitration.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler extends ExceptionHandlerExceptionResolver {

	private final Map<Class<?>, ApiException> exceptionMappings = new HashMap<>();

	public ExceptionHandler() {
		registerMapping(MissingServletRequestParameterException.class, 400, "Missing request parameter", BAD_REQUEST);
		registerMapping(MethodArgumentTypeMismatchException.class, 400, "Argument type mismatch", BAD_REQUEST);
		registerMapping(HttpRequestMethodNotSupportedException.class, 400, "HTTP method not supported",
				METHOD_NOT_ALLOWED);
		registerMapping(ServletRequestBindingException.class, 400, "Missing header in request", BAD_REQUEST);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiError(ApiException error, WebRequest request) {
		log.error(error.getStatusCode() + ": " + error.getMessage(), error);
		return new ResponseEntity<ErrorResponse>(error.getResponse(), new HttpHeaders(), error.getStatusCode());

	}

	protected void registerMapping(final Class<?> clazz, final int code, final String message,
			final HttpStatus status) {
		exceptionMappings.put(clazz, new ApiException(status, code, message));
	}

}
