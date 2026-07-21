package com.omeraksit.internshiptracker.domain;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternshipApplicationValidationTests {

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
	void validApplicationPassesValidation() {
		InternshipApplication application = createValidApplication();

		Set<ConstraintViolation<InternshipApplication>> violations = validator.validate(application);

		assertTrue(violations.isEmpty());
	}

	@Test
	void blankCompanyNameProducesValidationError() {
		InternshipApplication application = createValidApplication();
		application.setCompanyName(" ");

		Set<ConstraintViolation<InternshipApplication>> violations = validator.validate(application);

		assertHasViolationForField(violations, "companyName");
	}

	@Test
	void blankPositionTitleProducesValidationError() {
		InternshipApplication application = createValidApplication();
		application.setPositionTitle("");

		Set<ConstraintViolation<InternshipApplication>> violations = validator.validate(application);

		assertHasViolationForField(violations, "positionTitle");
	}

	@Test
	void invalidContactEmailProducesValidationError() {
		InternshipApplication application = createValidApplication();
		application.setContactEmail("invalid-email");

		Set<ConstraintViolation<InternshipApplication>> violations = validator.validate(application);

		assertHasViolationForField(violations, "contactEmail");
	}

	@Test
	void blankContactEmailPassesValidation() {
		InternshipApplication application = createValidApplication();
		application.setContactEmail("");

		Set<ConstraintViolation<InternshipApplication>> violations = validator.validate(application);

		assertTrue(violations.isEmpty());
	}

	@Test
	void newApplicationHasPlannedStatusByDefault() {
		InternshipApplication application = new InternshipApplication();

		assertEquals(ApplicationStatus.PLANNED, application.getStatus());
	}

	private static InternshipApplication createValidApplication() {
		InternshipApplication application = new InternshipApplication(
				"Example Technology",
				"Software Engineering Intern"
		);
		application.setContactEmail("student@example.com");
		return application;
	}

	private static void assertHasViolationForField(
			Set<ConstraintViolation<InternshipApplication>> violations,
			String fieldName
	) {
		boolean hasExpectedViolation = violations.stream()
				.anyMatch(violation -> violation.getPropertyPath().toString().equals(fieldName));

		assertTrue(hasExpectedViolation, "Expected a validation error for " + fieldName);
	}
}
