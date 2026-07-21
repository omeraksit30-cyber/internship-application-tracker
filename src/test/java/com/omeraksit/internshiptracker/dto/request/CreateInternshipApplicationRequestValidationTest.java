package com.omeraksit.internshiptracker.dto.request;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateInternshipApplicationRequestValidationTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	static void setUpValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void closeValidatorFactory() {
		validatorFactory.close();
	}

	@Test
	void validCreateRequestHasNoViolations() {
		CreateInternshipApplicationRequest request = createValidRequest();

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(violations.isEmpty());
	}

	@Test
	void blankCompanyNameProducesViolation() {
		CreateInternshipApplicationRequest request = createValidRequest();
		request.setCompanyName(" ");

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Company name is required"));
	}

	@Test
	void blankPositionTitleProducesViolation() {
		CreateInternshipApplicationRequest request = createValidRequest();
		request.setPositionTitle("");

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Position title is required"));
	}

	@Test
	void invalidEmailProducesViolation() {
		CreateInternshipApplicationRequest request = createValidRequest();
		request.setContactEmail("invalid-email");

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Contact email must be valid"));
	}

	@Test
	void nullEmailIsAccepted() {
		CreateInternshipApplicationRequest request = createValidRequest();
		request.setContactEmail(null);

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(violations.isEmpty());
	}

	@Test
	void overlyLongNotesProducesViolation() {
		CreateInternshipApplicationRequest request = createValidRequest();
		request.setNotes("a".repeat(3001));

		Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Notes must not exceed 3000 characters"));
	}

	private static CreateInternshipApplicationRequest createValidRequest() {
		CreateInternshipApplicationRequest request = new CreateInternshipApplicationRequest();
		request.setCompanyName("Example Tech");
		request.setPositionTitle("Backend Intern");
		request.setContactEmail("test@example.com");
		return request;
	}

	private static boolean hasMessage(
			Set<ConstraintViolation<CreateInternshipApplicationRequest>> violations,
			String expectedMessage) {
		return violations.stream()
				.anyMatch(violation -> expectedMessage.equals(violation.getMessage()));
	}
}
