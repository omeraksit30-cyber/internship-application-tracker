package com.omeraksit.internshiptracker.exception;

import java.time.LocalDate;

import com.omeraksit.internshiptracker.controller.InternshipApplicationController;
import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.dto.request.CreateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.request.UpdateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.mapper.InternshipApplicationMapper;
import com.omeraksit.internshiptracker.service.InternshipApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternshipApplicationController.class)
@Import({InternshipApplicationMapper.class, GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

	private static final String BASE_PATH = "/api/applications";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private InternshipApplicationService service;

	@Test
	void getByIdWhenApplicationDoesNotExistReturns404() throws Exception {
		when(service.getById(99L)).thenThrow(new ApplicationNotFoundException(99L));

		mockMvc.perform(get(BASE_PATH + "/{id}", 99L))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message", containsString("99")))
				.andExpect(jsonPath("$.path").value(BASE_PATH + "/99"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	@Test
	void deleteWhenApplicationDoesNotExistReturns404() throws Exception {
		doThrow(new ApplicationNotFoundException(99L)).when(service).delete(99L);

		mockMvc.perform(delete(BASE_PATH + "/{id}", 99L))
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.timestamp").isNotEmpty())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message", containsString("99")))
				.andExpect(jsonPath("$.path").value(BASE_PATH + "/99"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	@Test
	void invalidCreateRequestReturnsStructuredValidationError() throws Exception {
		CreateInternshipApplicationRequest request = createRequest();
		request.setCompanyName(" ");

		mockMvc.perform(post(BASE_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Validation failed"))
				.andExpect(jsonPath("$.path").value(BASE_PATH))
				.andExpect(jsonPath("$.fieldErrors.companyName")
						.value("Company name is required"));

		verify(service, never()).create(any(InternshipApplication.class));
	}

	@Test
	void invalidUpdateRequestReturnsStructuredValidationError() throws Exception {
		UpdateInternshipApplicationRequest request = updateRequest();
		request.setPositionTitle("");

		mockMvc.perform(put(BASE_PATH + "/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Validation failed"))
				.andExpect(jsonPath("$.path").value(BASE_PATH + "/1"))
				.andExpect(jsonPath("$.fieldErrors.positionTitle")
						.value("Position title is required"));

		verify(service, never()).update(anyLong(), any(InternshipApplication.class));
	}

	@Test
	void malformedJsonReturns400() throws Exception {
		String malformedJson = "{\"companyName\": ";

		mockMvc.perform(post(BASE_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(malformedJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message")
						.value("Malformed or unreadable request body"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	@Test
	void invalidEnumReturns400() throws Exception {
		String invalidEnumJson = """
				{
				  "companyName": "Example Tech",
				  "positionTitle": "Software Engineering Intern",
				  "status": "UNKNOWN_STATUS"
				}
				""";

		mockMvc.perform(post(BASE_PATH)
					.contentType(MediaType.APPLICATION_JSON)
					.content(invalidEnumJson))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Malformed or unreadable request body"))
				.andExpect(content().string(not(containsString("Cannot deserialize"))))
				.andExpect(content().string(not(containsString("UNKNOWN_STATUS"))));
	}

	@Test
	void illegalArgumentExceptionReturns400() throws Exception {
		when(service.getById(1L))
				.thenThrow(new IllegalArgumentException("Application id is invalid"));

		mockMvc.perform(get(BASE_PATH + "/{id}", 1L))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Application id is invalid"))
				.andExpect(jsonPath("$.path").value(BASE_PATH + "/1"))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	@Test
	void unexpectedExceptionReturns500WithoutLeakingDetails() throws Exception {
		when(service.searchApplications(null, null, null, 0, 10, "createdAt", "desc"))
				.thenThrow(new RuntimeException("Database password is secret"));

		mockMvc.perform(get(BASE_PATH))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("An unexpected error occurred"))
				.andExpect(jsonPath("$.path").value(BASE_PATH))
				.andExpect(jsonPath("$.fieldErrors").isEmpty())
				.andExpect(content().string(not(containsString("Database password is secret"))));
	}

	@Test
	void invalidStatusParameterReturns400() throws Exception {
		mockMvc.perform(get(BASE_PATH).param("status", "UNKNOWN"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message")
						.value("Invalid value for parameter: status"))
				.andExpect(jsonPath("$.path").value(BASE_PATH))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	@Test
	void invalidWorkModeParameterReturns400() throws Exception {
		mockMvc.perform(get(BASE_PATH).param("workMode", "SPACE"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message")
						.value("Invalid value for parameter: workMode"))
				.andExpect(jsonPath("$.path").value(BASE_PATH))
				.andExpect(jsonPath("$.fieldErrors").isEmpty());
	}

	private CreateInternshipApplicationRequest createRequest() {
		return new CreateInternshipApplicationRequest(
				"Example Tech",
				"Software Engineering Intern",
				ApplicationStatus.PLANNED,
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
				"Software Engineering Intern",
				ApplicationStatus.APPLIED,
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
}
