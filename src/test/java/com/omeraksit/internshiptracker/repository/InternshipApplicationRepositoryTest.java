package com.omeraksit.internshiptracker.repository;

import java.util.List;
import java.util.Optional;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

	@Test
	void searchWithoutFiltersReturnsAllApplications() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null, null, null, PageRequest.of(0, 10)
		);

		assertEquals(3, result.getTotalElements());
	}

	@Test
	void searchFiltersByStatus() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				ApplicationStatus.APPLIED, null, null, PageRequest.of(0, 10)
		);

		assertEquals(2, result.getTotalElements());
		assertTrue(result.getContent().stream()
				.allMatch(application -> application.getStatus() == ApplicationStatus.APPLIED));
	}

	@Test
	void searchFiltersByWorkMode() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null, WorkMode.REMOTE, null, PageRequest.of(0, 10)
		);

		assertEquals(1, result.getTotalElements());
		assertEquals("Example Tech", result.getContent().getFirst().getCompanyName());
	}

	@Test
	void searchMatchesCompanyNameIgnoringCase() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null, null, "example", PageRequest.of(0, 10)
		);

		assertEquals(1, result.getTotalElements());
		assertEquals("Example Tech", result.getContent().getFirst().getCompanyName());
	}

	@Test
	void searchMatchesPositionTitleIgnoringCase() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null, null, "SOFTWARE", PageRequest.of(0, 10)
		);

		assertEquals(3, result.getTotalElements());
		assertTrue(result.getContent().stream()
				.anyMatch(application -> application.getPositionTitle()
						.equals("Software Engineering Intern")));
		assertTrue(result.getContent().stream()
				.anyMatch(application -> application.getPositionTitle()
						.equals("Software Developer Intern")));
	}

	@Test
	void searchCombinesStatusAndWorkMode() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				ApplicationStatus.APPLIED,
				WorkMode.REMOTE,
				null,
				PageRequest.of(0, 10)
		);

		assertEquals(1, result.getTotalElements());
		assertEquals("Example Tech", result.getContent().getFirst().getCompanyName());
	}

	@Test
	void searchCombinesAllFilters() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				ApplicationStatus.APPLIED,
				WorkMode.ON_SITE,
				"developer",
				PageRequest.of(0, 10)
		);

		assertEquals(1, result.getTotalElements());
		assertEquals("Sample Bank", result.getContent().getFirst().getCompanyName());
	}

	@Test
	void searchReturnsEmptyPageWhenNoApplicationMatches() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null, null, "no-match", PageRequest.of(0, 10)
		);

		assertTrue(result.isEmpty());
	}

	@Test
	void searchSupportsPaginationAndSorting() {
		saveSearchApplications();

		Page<InternshipApplication> result = repository.search(
				null,
				null,
				null,
				PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "companyName"))
		);

		assertEquals(3, result.getTotalElements());
		assertEquals(2, result.getNumberOfElements());
		assertEquals("Demo Software", result.getContent().get(0).getCompanyName());
		assertEquals("Example Tech", result.getContent().get(1).getCompanyName());
	}

	private void saveSearchApplications() {
		InternshipApplication exampleApplication = createApplication(
				"Example Tech",
				"Software Engineering Intern"
		);
		exampleApplication.setStatus(ApplicationStatus.APPLIED);
		exampleApplication.setWorkMode(WorkMode.REMOTE);

		InternshipApplication demoApplication = createApplication(
				"Demo Software",
				"Data Intern"
		);
		demoApplication.setStatus(ApplicationStatus.HR_INTERVIEW);
		demoApplication.setWorkMode(WorkMode.HYBRID);

		InternshipApplication sampleApplication = createApplication(
				"Sample Bank",
				"Software Developer Intern"
		);
		sampleApplication.setStatus(ApplicationStatus.APPLIED);
		sampleApplication.setWorkMode(WorkMode.ON_SITE);

		repository.saveAllAndFlush(List.of(
				exampleApplication,
				demoApplication,
				sampleApplication
		));
		entityManager.clear();
	}

	private InternshipApplication createApplication(String companyName, String positionTitle) {
		InternshipApplication application = new InternshipApplication(companyName, positionTitle);
		application.setContactEmail("test@example.com");
		return application;
	}
}
