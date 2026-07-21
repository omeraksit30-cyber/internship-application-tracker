package com.omeraksit.internshiptracker.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.dto.request.CreateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.request.UpdateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.response.InternshipApplicationResponse;
import com.omeraksit.internshiptracker.dto.response.PagedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternshipApplicationMapperTest {

	private InternshipApplicationMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new InternshipApplicationMapper();
	}

	@Test
	void createRequestMapsToEntity() {
		CreateInternshipApplicationRequest request = createRequest(ApplicationStatus.APPLIED);

		InternshipApplication entity = mapper.toEntity(request);

		assertEditableFields(
				entity,
				"Example Tech",
				"Software Engineering Intern",
				ApplicationStatus.APPLIED,
				LocalDate.of(2026, 7, 1),
				LocalDate.of(2026, 7, 8),
				"Test notes"
		);
		assertNull(entity.getId());
		assertNull(entity.getCreatedAt());
		assertNull(entity.getUpdatedAt());
	}

	@Test
	void createRequestDefaultsStatusToPlanned() {
		CreateInternshipApplicationRequest request = createRequest(null);

		InternshipApplication entity = mapper.toEntity(request);

		assertEquals(ApplicationStatus.PLANNED, entity.getStatus());
	}

	@Test
	void createRequestPreservesProvidedStatus() {
		CreateInternshipApplicationRequest request = createRequest(ApplicationStatus.APPLIED);

		InternshipApplication entity = mapper.toEntity(request);

		assertEquals(ApplicationStatus.APPLIED, entity.getStatus());
	}

	@Test
	void nullCreateRequestThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> mapper.toEntity((CreateInternshipApplicationRequest) null)
		);
	}

	@Test
	void updateRequestMapsToEntity() {
		UpdateInternshipApplicationRequest request = updateRequest(ApplicationStatus.OFFER);

		InternshipApplication entity = mapper.toEntity(request);

		assertEditableFields(
				entity,
				"Demo Software",
				"Software Engineering Intern",
				ApplicationStatus.OFFER,
				LocalDate.of(2026, 7, 2),
				LocalDate.of(2026, 7, 9),
				"Updated test notes"
		);
		assertNull(entity.getId());
		assertNull(entity.getCreatedAt());
		assertNull(entity.getUpdatedAt());
	}

	@Test
	void updateRequestDoesNotDefaultNullStatus() {
		UpdateInternshipApplicationRequest request = updateRequest(null);

		InternshipApplication entity = mapper.toEntity(request);

		assertNull(entity.getStatus());
	}

	@Test
	void nullUpdateRequestThrowsException() {
		assertThrows(
				IllegalArgumentException.class,
				() -> mapper.toEntity((UpdateInternshipApplicationRequest) null)
		);
	}

	@Test
	void entityMapsToResponse() {
		InternshipApplication entity = entity(
				1L,
				"Example Tech",
				ApplicationStatus.TECHNICAL_INTERVIEW,
				LocalDateTime.of(2026, 7, 1, 10, 0),
				LocalDateTime.of(2026, 7, 3, 11, 30)
		);

		InternshipApplicationResponse response = mapper.toResponse(entity);

		assertResponseFields(response, entity);
	}

	@Test
	void nullEntityThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> mapper.toResponse(null));
	}

	@Test
	void entityListMapsToResponseList() {
		InternshipApplication firstEntity = entity(
				1L,
				"Example Tech",
				ApplicationStatus.APPLIED,
				LocalDateTime.of(2026, 7, 1, 10, 0),
				LocalDateTime.of(2026, 7, 1, 10, 0)
		);
		InternshipApplication secondEntity = entity(
				2L,
				"Demo Software",
				ApplicationStatus.HR_INTERVIEW,
				LocalDateTime.of(2026, 7, 2, 10, 0),
				LocalDateTime.of(2026, 7, 4, 14, 0)
		);

		List<InternshipApplicationResponse> responses =
				mapper.toResponseList(List.of(firstEntity, secondEntity));

		assertEquals(2, responses.size());
		assertResponseFields(responses.get(0), firstEntity);
		assertResponseFields(responses.get(1), secondEntity);
	}

	@Test
	void emptyEntityListReturnsEmptyList() {
		List<InternshipApplicationResponse> responses = mapper.toResponseList(List.of());

		assertNotNull(responses);
		assertTrue(responses.isEmpty());
		responses.add(new InternshipApplicationResponse());
		assertFalse(responses.isEmpty());
	}

	@Test
	void nullEntityListThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> mapper.toResponseList(null));
	}

	@Test
	void listContainingNullEntityThrowsException() {
		List<InternshipApplication> entities = new ArrayList<>();
		entities.add(entity(
				1L,
				"Example Tech",
				ApplicationStatus.APPLIED,
				LocalDateTime.of(2026, 7, 1, 10, 0),
				LocalDateTime.of(2026, 7, 1, 10, 0)
		));
		entities.add(null);

		assertThrows(IllegalArgumentException.class, () -> mapper.toResponseList(entities));
	}

	@Test
	void entityPageMapsToPagedResponse() {
		InternshipApplication firstEntity = entity(
				1L,
				"Example Tech",
				ApplicationStatus.APPLIED,
				LocalDateTime.of(2026, 7, 1, 10, 0),
				LocalDateTime.of(2026, 7, 1, 10, 0)
		);
		InternshipApplication secondEntity = entity(
				2L,
				"Demo Software",
				ApplicationStatus.HR_INTERVIEW,
				LocalDateTime.of(2026, 7, 2, 10, 0),
				LocalDateTime.of(2026, 7, 3, 12, 0)
		);
		Page<InternshipApplication> entityPage = new PageImpl<>(
				List.of(firstEntity, secondEntity),
				PageRequest.of(1, 2),
				5
		);

		PagedResponse<InternshipApplicationResponse> response =
				mapper.toPagedResponse(entityPage);

		assertEquals(2, response.getContent().size());
		assertResponseFields(response.getContent().get(0), firstEntity);
		assertResponseFields(response.getContent().get(1), secondEntity);
		assertEquals(1, response.getPage());
		assertEquals(2, response.getSize());
		assertEquals(5, response.getTotalElements());
		assertEquals(3, response.getTotalPages());
		assertEquals(2, response.getNumberOfElements());
		assertFalse(response.isFirst());
		assertFalse(response.isLast());
		assertFalse(response.isEmpty());
	}

	@Test
	void emptyEntityPageMapsToEmptyPagedResponse() {
		Page<InternshipApplication> entityPage = Page.empty(PageRequest.of(0, 10));

		PagedResponse<InternshipApplicationResponse> response =
				mapper.toPagedResponse(entityPage);

		assertNotNull(response.getContent());
		assertTrue(response.getContent().isEmpty());
		assertTrue(response.isEmpty());
	}

	@Test
	void nullPageThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> mapper.toPagedResponse(null));
	}

	private CreateInternshipApplicationRequest createRequest(ApplicationStatus status) {
		return new CreateInternshipApplicationRequest(
				"Example Tech",
				"Software Engineering Intern",
				status,
				WorkMode.HYBRID,
				"Test City",
				LocalDate.of(2026, 7, 1),
				LocalDate.of(2026, 7, 8),
				"Test Contact",
				"test@example.com",
				"https://example.com/jobs/1",
				"Test notes"
		);
	}

	private UpdateInternshipApplicationRequest updateRequest(ApplicationStatus status) {
		return new UpdateInternshipApplicationRequest(
				"Demo Software",
				"Software Engineering Intern",
				status,
				WorkMode.HYBRID,
				"Test City",
				LocalDate.of(2026, 7, 2),
				LocalDate.of(2026, 7, 9),
				"Test Contact",
				"test@example.com",
				"https://example.com/jobs/1",
				"Updated test notes"
		);
	}

	private InternshipApplication entity(
			Long id,
			String companyName,
			ApplicationStatus status,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		InternshipApplication entity = new InternshipApplication();
		entity.setId(id);
		entity.setCompanyName(companyName);
		entity.setPositionTitle("Software Engineering Intern");
		entity.setStatus(status);
		entity.setWorkMode(WorkMode.REMOTE);
		entity.setLocation("Test City");
		entity.setApplicationDate(LocalDate.of(2026, 7, 1));
		entity.setFollowUpDate(LocalDate.of(2026, 7, 8));
		entity.setContactName("Test Contact");
		entity.setContactEmail("test@example.com");
		entity.setJobUrl("https://example.com/jobs/1");
		entity.setNotes("Test notes");
		entity.setCreatedAt(createdAt);
		entity.setUpdatedAt(updatedAt);
		return entity;
	}

	private void assertEditableFields(
			InternshipApplication entity,
			String expectedCompanyName,
			String expectedPositionTitle,
			ApplicationStatus expectedStatus,
			LocalDate expectedApplicationDate,
			LocalDate expectedFollowUpDate,
			String expectedNotes) {
		assertEquals(expectedCompanyName, entity.getCompanyName());
		assertEquals(expectedPositionTitle, entity.getPositionTitle());
		assertEquals(expectedStatus, entity.getStatus());
		assertEquals(WorkMode.HYBRID, entity.getWorkMode());
		assertEquals("Test City", entity.getLocation());
		assertEquals(expectedApplicationDate, entity.getApplicationDate());
		assertEquals(expectedFollowUpDate, entity.getFollowUpDate());
		assertEquals("Test Contact", entity.getContactName());
		assertEquals("test@example.com", entity.getContactEmail());
		assertEquals("https://example.com/jobs/1", entity.getJobUrl());
		assertEquals(expectedNotes, entity.getNotes());
	}

	private void assertResponseFields(
			InternshipApplicationResponse response,
			InternshipApplication entity) {
		assertEquals(entity.getId(), response.getId());
		assertEquals(entity.getCompanyName(), response.getCompanyName());
		assertEquals(entity.getPositionTitle(), response.getPositionTitle());
		assertEquals(entity.getStatus(), response.getStatus());
		assertEquals(entity.getWorkMode(), response.getWorkMode());
		assertEquals(entity.getLocation(), response.getLocation());
		assertEquals(entity.getApplicationDate(), response.getApplicationDate());
		assertEquals(entity.getFollowUpDate(), response.getFollowUpDate());
		assertEquals(entity.getContactName(), response.getContactName());
		assertEquals(entity.getContactEmail(), response.getContactEmail());
		assertEquals(entity.getJobUrl(), response.getJobUrl());
		assertEquals(entity.getNotes(), response.getNotes());
		assertEquals(entity.getCreatedAt(), response.getCreatedAt());
		assertEquals(entity.getUpdatedAt(), response.getUpdatedAt());
	}
}
