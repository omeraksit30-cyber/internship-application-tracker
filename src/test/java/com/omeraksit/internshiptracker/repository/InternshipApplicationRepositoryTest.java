package com.omeraksit.internshiptracker.repository;

import java.util.List;
import java.util.Optional;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class InternshipApplicationRepositoryTest {

	@Autowired
	private InternshipApplicationRepository repository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void saveAndFindById() {
		InternshipApplication application = createApplication(
				"Example Tech",
				"Software Engineering Intern"
		);

		InternshipApplication savedApplication = repository.saveAndFlush(application);
		Long applicationId = savedApplication.getId();
		entityManager.clear();

		assertNotNull(applicationId);

		Optional<InternshipApplication> foundApplication = repository.findById(applicationId);

		assertTrue(foundApplication.isPresent());
		assertEquals("Example Tech", foundApplication.get().getCompanyName());
		assertEquals("Software Engineering Intern", foundApplication.get().getPositionTitle());
	}

	@Test
	void updateApplication() {
		InternshipApplication application = createApplication(
				"Demo Software",
				"Backend Developer Intern"
		);
		InternshipApplication savedApplication = repository.saveAndFlush(application);

		savedApplication.setStatus(ApplicationStatus.APPLIED);
		repository.saveAndFlush(savedApplication);
		Long applicationId = savedApplication.getId();
		entityManager.clear();

		InternshipApplication updatedApplication = repository.findById(applicationId).orElseThrow();

		assertEquals(ApplicationStatus.APPLIED, updatedApplication.getStatus());
		assertNotNull(updatedApplication.getUpdatedAt());
	}

	@Test
	void deleteApplication() {
		InternshipApplication application = createApplication(
				"Test Company",
				"Quality Assurance Intern"
		);
		InternshipApplication savedApplication = repository.saveAndFlush(application);
		Long applicationId = savedApplication.getId();

		repository.delete(savedApplication);
		repository.flush();
		entityManager.clear();

		Optional<InternshipApplication> deletedApplication = repository.findById(applicationId);

		assertFalse(deletedApplication.isPresent());
	}

	@Test
	void findAllApplications() {
		InternshipApplication firstApplication = createApplication(
				"Example Tech",
				"Software Engineering Intern"
		);
		InternshipApplication secondApplication = createApplication(
				"Demo Software",
				"Data Engineering Intern"
		);

		repository.saveAllAndFlush(List.of(firstApplication, secondApplication));
		entityManager.clear();

		List<InternshipApplication> applications = repository.findAll();

		assertEquals(2, applications.size());
	}

	private InternshipApplication createApplication(String companyName, String positionTitle) {
		InternshipApplication application = new InternshipApplication(companyName, positionTitle);
		application.setContactEmail("test@example.com");
		return application;
	}
}
