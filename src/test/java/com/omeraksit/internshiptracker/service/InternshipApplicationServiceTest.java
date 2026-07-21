package com.omeraksit.internshiptracker.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.exception.ApplicationNotFoundException;
import com.omeraksit.internshiptracker.repository.InternshipApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternshipApplicationServiceTest {

	@Mock
	private InternshipApplicationRepository repository;

	@InjectMocks
	private InternshipApplicationService service;

	@Test
	void createSavesApplication() {
		InternshipApplication application = createApplication("Example Tech", "Backend Intern");
		application.setId(99L);
		when(repository.save(any(InternshipApplication.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		InternshipApplication result = service.create(application);

		ArgumentCaptor<InternshipApplication> applicationCaptor =
				ArgumentCaptor.forClass(InternshipApplication.class);
		verify(repository).save(applicationCaptor.capture());
		assertSame(application, result);
		assertNull(applicationCaptor.getValue().getId());
	}

	@Test
	void createRejectsNullApplication() {
		assertThrows(IllegalArgumentException.class, () -> service.create(null));

		verifyNoInteractions(repository);
	}

	@Test
	void getPageReturnsRepositoryPage() {
		List<InternshipApplication> applications = List.of(
				createApplication("Example Tech", "Backend Intern"),
				createApplication("Demo Software", "Data Engineering Intern")
		);
		Page<InternshipApplication> repositoryPage = new PageImpl<>(applications);
		when(repository.findAll(any(Pageable.class))).thenReturn(repositoryPage);

		Page<InternshipApplication> result =
				service.getPage(0, 10, "createdAt", "desc");

		assertSame(repositoryPage, result);
		verify(repository).findAll(any(Pageable.class));
	}

	@Test
	void getPageCreatesExpectedPageable() {
		when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		service.getPage(2, 15, "companyName", "asc");

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(repository).findAll(pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		assertEquals(2, pageable.getPageNumber());
		assertEquals(15, pageable.getPageSize());
		assertEquals(
				Sort.Direction.ASC,
				pageable.getSort().getOrderFor("companyName").getDirection()
		);
	}

	@Test
	void getPageAcceptsCaseInsensitiveDirection() {
		when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		service.getPage(0, 10, "createdAt", "DESC");

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(repository).findAll(pageableCaptor.capture());
		assertEquals(
				Sort.Direction.DESC,
				pageableCaptor.getValue().getSort().getOrderFor("createdAt").getDirection()
		);
	}

	@Test
	void getPageRejectsNegativePage() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.getPage(-1, 10, "createdAt", "desc")
		);

		assertEquals("Page must be zero or greater", exception.getMessage());
		verify(repository, never()).findAll(any(Pageable.class));
	}

	@Test
	void getPageRejectsZeroSize() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.getPage(0, 0, "createdAt", "desc")
		);

		assertEquals("Size must be at least 1", exception.getMessage());
		verify(repository, never()).findAll(any(Pageable.class));
	}

	@Test
	void getPageRejectsSizeOverMaximum() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.getPage(0, 101, "createdAt", "desc")
		);

		assertEquals("Size must not exceed 100", exception.getMessage());
		verify(repository, never()).findAll(any(Pageable.class));
	}

	@Test
	void getPageRejectsUnsupportedSortField() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.getPage(0, 10, "password", "desc")
		);

		assertEquals("Unsupported sort field: password", exception.getMessage());
		verify(repository, never()).findAll(any(Pageable.class));
	}

	@Test
	void getPageRejectsInvalidDirection() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.getPage(0, 10, "createdAt", "sideways")
		);

		assertEquals("Direction must be either asc or desc", exception.getMessage());
		verify(repository, never()).findAll(any(Pageable.class));
	}

	@Test
	void getByIdReturnsExistingApplication() {
		Long applicationId = 10L;
		InternshipApplication application = createApplication("Example Tech", "Backend Intern");
		application.setId(applicationId);
		when(repository.findById(applicationId)).thenReturn(Optional.of(application));

		InternshipApplication result = service.getById(applicationId);

		assertSame(application, result);
		verify(repository).findById(applicationId);
	}

	@Test
	void getByIdThrowsWhenApplicationDoesNotExist() {
		Long applicationId = 15L;
		when(repository.findById(applicationId)).thenReturn(Optional.empty());

		ApplicationNotFoundException exception = assertThrows(
				ApplicationNotFoundException.class,
				() -> service.getById(applicationId)
		);

		assertTrue(exception.getMessage().contains(applicationId.toString()));
		verify(repository).findById(applicationId);
	}

	@Test
	void getByIdRejectsNullId() {
		assertThrows(IllegalArgumentException.class, () -> service.getById(null));

		verifyNoInteractions(repository);
	}

	@Test
	void updateCopiesEditableFieldsAndPreservesIdentity() {
		Long applicationId = 20L;
		LocalDateTime createdAt = LocalDateTime.of(2026, 1, 10, 9, 30);
		InternshipApplication existingApplication = createApplication(
				"Example Tech",
				"Backend Intern"
		);
		existingApplication.setId(applicationId);
		existingApplication.setCreatedAt(createdAt);

		InternshipApplication updatedApplication = createApplication(
				"Demo Software",
				"Platform Engineering Intern"
		);
		updatedApplication.setId(999L);
		updatedApplication.setCreatedAt(LocalDateTime.of(2030, 2, 1, 12, 0));
		updatedApplication.setStatus(ApplicationStatus.TECHNICAL_INTERVIEW);
		updatedApplication.setWorkMode(WorkMode.HYBRID);
		updatedApplication.setLocation("Test City");
		updatedApplication.setApplicationDate(LocalDate.of(2026, 2, 2));
		updatedApplication.setFollowUpDate(LocalDate.of(2026, 2, 9));
		updatedApplication.setContactName("Test Contact");
		updatedApplication.setContactEmail("contact@example.com");
		updatedApplication.setJobUrl("https://example.com/jobs/platform-intern");
		updatedApplication.setNotes("Updated test notes");

		when(repository.findById(applicationId)).thenReturn(Optional.of(existingApplication));
		when(repository.save(existingApplication)).thenReturn(existingApplication);

		InternshipApplication result = service.update(applicationId, updatedApplication);

		assertSame(existingApplication, result);
		assertEquals(applicationId, result.getId());
		assertEquals(createdAt, result.getCreatedAt());
		assertEquals("Demo Software", result.getCompanyName());
		assertEquals("Platform Engineering Intern", result.getPositionTitle());
		assertEquals(ApplicationStatus.TECHNICAL_INTERVIEW, result.getStatus());
		assertEquals(WorkMode.HYBRID, result.getWorkMode());
		assertEquals("Test City", result.getLocation());
		assertEquals(LocalDate.of(2026, 2, 2), result.getApplicationDate());
		assertEquals(LocalDate.of(2026, 2, 9), result.getFollowUpDate());
		assertEquals("Test Contact", result.getContactName());
		assertEquals("contact@example.com", result.getContactEmail());
		assertEquals("https://example.com/jobs/platform-intern", result.getJobUrl());
		assertEquals("Updated test notes", result.getNotes());
		verify(repository).save(existingApplication);
	}

	@Test
	void updateThrowsWhenApplicationDoesNotExist() {
		Long applicationId = 25L;
		InternshipApplication updatedApplication = createApplication(
				"Demo Software",
				"Backend Intern"
		);
		when(repository.findById(applicationId)).thenReturn(Optional.empty());

		assertThrows(
				ApplicationNotFoundException.class,
				() -> service.update(applicationId, updatedApplication)
		);

		verify(repository).findById(applicationId);
		verify(repository, never()).save(any(InternshipApplication.class));
	}

	@Test
	void updateRejectsNullId() {
		InternshipApplication updatedApplication = createApplication(
				"Demo Software",
				"Backend Intern"
		);

		assertThrows(
				IllegalArgumentException.class,
				() -> service.update(null, updatedApplication)
		);

		verifyNoInteractions(repository);
	}

	@Test
	void updateRejectsNullApplication() {
		assertThrows(IllegalArgumentException.class, () -> service.update(30L, null));

		verifyNoInteractions(repository);
	}

	@Test
	void deleteRemovesExistingApplication() {
		Long applicationId = 35L;
		InternshipApplication application = createApplication("Test Company", "QA Intern");
		application.setId(applicationId);
		when(repository.findById(applicationId)).thenReturn(Optional.of(application));

		service.delete(applicationId);

		verify(repository).findById(applicationId);
		verify(repository).delete(application);
	}

	@Test
	void deleteThrowsWhenApplicationDoesNotExist() {
		Long applicationId = 40L;
		when(repository.findById(applicationId)).thenReturn(Optional.empty());

		assertThrows(ApplicationNotFoundException.class, () -> service.delete(applicationId));

		verify(repository).findById(applicationId);
		verify(repository, never()).delete(any(InternshipApplication.class));
	}

	@Test
	void deleteRejectsNullId() {
		assertThrows(IllegalArgumentException.class, () -> service.delete(null));

		verifyNoInteractions(repository);
	}

	private InternshipApplication createApplication(String companyName, String positionTitle) {
		InternshipApplication application = new InternshipApplication(companyName, positionTitle);
		application.setContactEmail("test@example.com");
		return application;
	}
}
