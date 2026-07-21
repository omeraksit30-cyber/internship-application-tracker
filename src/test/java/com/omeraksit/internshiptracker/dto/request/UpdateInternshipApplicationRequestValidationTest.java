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

class UpdateInternshipApplicationRequestValidationTest {

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
	void validUpdateRequestHasNoViolations() {
		UpdateInternshipApplicationRequest request = createValidRequest();

		Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(violations.isEmpty());
	}

	@Test
	void blankCompanyNameProducesViolation() {
		UpdateInternshipApplicationRequest request = createValidRequest();
		request.setCompanyName(" ");

		Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Company name is required"));
	}

	@Test
	void blankPositionTitleProducesViolation() {
		UpdateInternshipApplicationRequest request = createValidRequest();
		request.setPositionTitle("");

		Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Position title is required"));
	}

	@Test
	void invalidEmailProducesViolation() {
		UpdateInternshipApplicationRequest request = createValidRequest();
		request.setContactEmail("invalid-email");

		Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Contact email must be valid"));
	}

	@Test
	void overlyLongJobUrlProducesViolation() {
		UpdateInternshipApplicationRequest request = createValidRequest();
		request.setJobUrl("a".repeat(501));

		Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations =
				validator.validate(request);

		assertTrue(hasMessage(violations, "Job URL must not exceed 500 characters"));
	}

	private static UpdateInternshipApplicationRequest createValidRequest() {
		UpdateInternshipApplicationRequest request = new UpdateInternshipApplicationRequest();
		request.setCompanyName("Demo Software");
		request.setPositionTitle("Software Engineering Intern");
		request.setContactEmail("test@example.com");
		return request;
	}

	private static boolean hasMessage(
			Set<ConstraintViolation<UpdateInternshipApplicationRequest>> violations,
			String expectedMessage) {
		return violations.stream()
				.anyMatch(violation -> expectedMessage.equals(violation.getMessage()));
	}
}
