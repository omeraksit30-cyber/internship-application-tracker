package com.omeraksit.internshiptracker.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.dto.request.CreateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.request.UpdateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.exception.GlobalExceptionHandler;
import com.omeraksit.internshiptracker.mapper.InternshipApplicationMapper;
import com.omeraksit.internshiptracker.service.InternshipApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternshipApplicationController.class)
@Import({InternshipApplicationMapper.class, GlobalExceptionHandler.class})
class InternshipApplicationControllerTest {

	private static final String BASE_PATH = "/api/applications";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private InternshipApplicationService service;

	@Test
	void createReturns201AndCreatedApplication() throws Exception {
		CreateInternshipApplicationRequest request = createRequest();
		InternshipApplication savedEntity = entity(
				1L,
				"Example Tech",
				"Software Engineering Intern",
				ApplicationStatus.PLANNED,
				WorkMode.HYBRID
		);
		when(service.create(any(InternshipApplication.class))).thenReturn(savedEntity);

		mockMvc.perform(post(BASE_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(header().string("Location", BASE_PATH + "/1"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.companyName").value("Example Tech"))
				.andExpect(jsonPath("$.positionTitle").value("Software Engineering Intern"))
				.andExpect(jsonPath("$.status").value("PLANNED"))
				.andExpect(jsonPath("$.workMode").value("HYBRID"));

		ArgumentCaptor<InternshipApplication> captor =
				ArgumentCaptor.forClass(InternshipApplication.class);
		verify(service).create(captor.capture());
		assertNull(captor.getValue().getId());
		assertEquals("Example Tech", captor.getValue().getCompanyName());
		assertEquals("Software Engineering Intern", captor.getValue().getPositionTitle());
	}

	@Test
	void createWithInvalidRequestReturns400() throws Exception {
		CreateInternshipApplicationRequest request = createRequest();
		request.setCompanyName(" ");

		mockMvc.perform(post(BASE_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());

		verify(service, never()).create(any(InternshipApplication.class));
	}

	@Test
	void getApplicationsReturnsPagedResponse() throws Exception {
		InternshipApplication firstEntity = entity(
				1L,
				"Example Tech",
				"Software Engineering Intern",
				ApplicationStatus.APPLIED,
				WorkMode.HYBRID
		);
		InternshipApplication secondEntity = entity(
				2L,
				"Demo Software",
				"Backend Intern",
				ApplicationStatus.HR_INTERVIEW,
				WorkMode.REMOTE
		);
		Page<InternshipApplication> applicationPage = new PageImpl<>(
				List.of(firstEntity, secondEntity),
				PageRequest.of(0, 10),
				2
		);
		when(service.getPage(0, 10, "createdAt", "desc")).thenReturn(applicationPage);

		mockMvc.perform(get(BASE_PATH)
					.param("page", "0")
					.param("size", "10")
					.param("sortBy", "createdAt")
					.param("direction", "desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.content[0].companyName").value("Example Tech"))
				.andExpect(jsonPath("$.content[1].companyName").value("Demo Software"))
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.numberOfElements").value(2))
				.andExpect(jsonPath("$.first").value(true))
				.andExpect(jsonPath("$.last").value(true))
				.andExpect(jsonPath("$.empty").value(false));

		verify(service).getPage(0, 10, "createdAt", "desc");
	}

	@Test
	void getApplicationsUsesDefaultPaginationParameters() throws Exception {
		when(service.getPage(0, 10, "createdAt", "desc"))
				.thenReturn(Page.empty(PageRequest.of(0, 10)));

		mockMvc.perform(get(BASE_PATH))
				.andExpect(status().isOk());

		verify(service).getPage(0, 10, "createdAt", "desc");
	}

	@Test
	void getApplicationsReturnsEmptyPagedResponse() throws Exception {
		when(service.getPage(0, 10, "createdAt", "desc"))
				.thenReturn(Page.empty(PageRequest.of(0, 10)));

		mockMvc.perform(get(BASE_PATH))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content").isEmpty())
				.andExpect(jsonPath("$.empty").value(true));
	}

	@Test
	void invalidPaginationReturnsStructured400() throws Exception {
		when(service.getPage(0, 101, "createdAt", "desc"))
				.thenThrow(new IllegalArgumentException("Size must not exceed 100"));

		mockMvc.perform(get(BASE_PATH).param("size", "101"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Size must not exceed 100"))
				.andExpect(jsonPath("$.path").value(BASE_PATH));
	}

	@Test
	void getByIdReturnsApplication() throws Exception {
		InternshipApplication application = entity(
				1L,
				"Example Tech",
				"Software Engineering Intern",
				ApplicationStatus.APPLIED,
				WorkMode.HYBRID
		);
		when(service.getById(1L)).thenReturn(application);

		mockMvc.perform(get(BASE_PATH + "/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.companyName").value("Example Tech"))
				.andExpect(jsonPath("$.positionTitle").value("Software Engineering Intern"));

		verify(service).getById(1L);
	}

	@Test
	void updateReturnsUpdatedApplication() throws Exception {
		UpdateInternshipApplicationRequest request = updateRequest();
		InternshipApplication updatedEntity = entity(
				1L,
				"Demo Software",
				"Backend Intern",
				ApplicationStatus.TECHNICAL_INTERVIEW,
				WorkMode.REMOTE
		);
		when(service.update(anyLong(), any(InternshipApplication.class)))
				.thenReturn(updatedEntity);

		mockMvc.perform(put(BASE_PATH + "/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.companyName").value("Demo Software"))
				.andExpect(jsonPath("$.positionTitle").value("Backend Intern"))
				.andExpect(jsonPath("$.status").value("TECHNICAL_INTERVIEW"));

		ArgumentCaptor<InternshipApplication> captor =
				ArgumentCaptor.forClass(InternshipApplication.class);
		verify(service).update(org.mockito.ArgumentMatchers.eq(1L), captor.capture());
		assertNull(captor.getValue().getId());
		assertEquals("Demo Software", captor.getValue().getCompanyName());
		assertEquals("Backend Intern", captor.getValue().getPositionTitle());
		assertEquals(ApplicationStatus.TECHNICAL_INTERVIEW, captor.getValue().getStatus());
		assertEquals(WorkMode.REMOTE, captor.getValue().getWorkMode());
	}

	@Test
	void updateWithInvalidRequestReturns400() throws Exception {
		UpdateInternshipApplicationRequest request = updateRequest();
		request.setPositionTitle("");

		mockMvc.perform(put(BASE_PATH + "/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());

		verify(service, never()).update(anyLong(), any(InternshipApplication.class));
	}

	@Test
	void deleteReturns204() throws Exception {
		mockMvc.perform(delete(BASE_PATH + "/{id}", 1L))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(service).delete(1L);
	}

	private CreateInternshipApplicationRequest createRequest() {
		return new CreateInternshipApplicationRequest(
				"Example Tech",
				"Software Engineering Intern",
				null,
				WorkMode.HYBRID,
				"Test City",
				LocalDate.of(2026, 7, 21),
				LocalDate.of(2026, 7, 28),
				"Test Contact",
				"test@example.com",
				"https://example.com/jobs/1",
				"Test notes"
		);
	}

	private UpdateInternshipApplicationRequest updateRequest() {
		return new UpdateInternshipApplicationRequest(
				"Demo Software",
				"Backend Intern",
				ApplicationStatus.TECHNICAL_INTERVIEW,
				WorkMode.REMOTE,
				"Remote",
				LocalDate.of(2026, 7, 21),
				LocalDate.of(2026, 7, 30),
				"Test Contact",
				"test@example.com",
				"https://example.com/jobs/1",
				"Updated test notes"
		);
	}

	private InternshipApplication entity(
			Long id,
			String companyName,
			String positionTitle,
			ApplicationStatus status,
			WorkMode workMode) {
		InternshipApplication entity = new InternshipApplication();
		entity.setId(id);
		entity.setCompanyName(companyName);
		entity.setPositionTitle(positionTitle);
		entity.setStatus(status);
		entity.setWorkMode(workMode);
		entity.setLocation("Test City");
		entity.setApplicationDate(LocalDate.of(2026, 7, 21));
		entity.setFollowUpDate(LocalDate.of(2026, 7, 28));
		entity.setContactName("Test Contact");
		entity.setContactEmail("test@example.com");
		entity.setJobUrl("https://example.com/jobs/1");
		entity.setNotes("Test notes");
		entity.setCreatedAt(LocalDateTime.of(2026, 7, 21, 10, 0));
		entity.setUpdatedAt(LocalDateTime.of(2026, 7, 21, 10, 0));
		return entity;
	}
}
