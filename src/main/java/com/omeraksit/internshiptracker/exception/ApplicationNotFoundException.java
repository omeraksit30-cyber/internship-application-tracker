package com.omeraksit.internshiptracker.exception;

public class ApplicationNotFoundException extends RuntimeException {

	public ApplicationNotFoundException(Long id) {
		super("Internship application not found with id: " + id);
	}
}
