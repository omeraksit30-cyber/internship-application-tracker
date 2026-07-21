package com.omeraksit.internshiptracker.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import com.omeraksit.internshiptracker.dto.error.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApplicationNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleApplicationNotFound(
			ApplicationNotFoundException exception,
			HttpServletRequest request) {
		return buildResponse(
				HttpStatus.NOT_FOUND,
				exception.getMessage(),
				request.getRequestURI(),
				new LinkedHashMap<>()
		);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationFailure(
			MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			String validationMessage = fieldError.getDefaultMessage();
			if (validationMessage == null || validationMessage.isBlank()) {
				validationMessage = "Invalid value";
			}
			fieldErrors.putIfAbsent(fieldError.getField(), validationMessage);
		}

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"Validation failed",
				request.getRequestURI(),
				fieldErrors
		);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
			IllegalArgumentException exception,
			HttpServletRequest request) {
		String message = exception.getMessage();
		if (message == null || message.isBlank()) {
			message = "Invalid request";
		}

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				message,
				request.getRequestURI(),
				new LinkedHashMap<>()
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
			HttpMessageNotReadableException exception,
			HttpServletRequest request) {
		return buildResponse(
				HttpStatus.BAD_REQUEST,
				"Malformed or unreadable request body",
				request.getRequestURI(),
				new LinkedHashMap<>()
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
			Exception exception,
			HttpServletRequest request) {
		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred",
				request.getRequestURI(),
				new LinkedHashMap<>()
		);
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(
			HttpStatus status,
			String message,
			String path,
			Map<String, String> fieldErrors) {
		ApiErrorResponse response = new ApiErrorResponse(
				Instant.now(),
				status.value(),
				status.getReasonPhrase(),
				message,
				path,
				fieldErrors
		);

		return ResponseEntity.status(status).body(response);
	}
}
