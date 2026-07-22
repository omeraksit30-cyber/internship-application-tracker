package com.omeraksit.internshiptracker.documentation;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void openApiDocumentIsAvailable() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.openapi").exists())
				.andExpect(jsonPath("$.info.title").value("Internship Application Tracker API"))
				.andExpect(jsonPath("$.info.version").value("v1"));
	}

	@Test
	void allApplicationPathsAreDocumented() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.paths['/api/applications'].get").exists())
				.andExpect(jsonPath("$.paths['/api/applications'].post").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].get").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].put").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].delete").exists());
	}

	@Test
	void searchParametersAreDocumented() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath(
						"$.paths['/api/applications'].get.parameters[*].name",
						hasItems("status", "workMode", "search", "page", "size", "sortBy", "direction")
				));
	}

	@Test
	void expectedResponsesAreDocumented() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.paths['/api/applications'].post.responses['201']").exists())
				.andExpect(jsonPath("$.paths['/api/applications'].post.responses['400']").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].get.responses['200']").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].get.responses['404']").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].delete.responses['204']").exists())
				.andExpect(jsonPath("$.paths['/api/applications/{id}'].delete.responses['404']").exists());
	}

	@Test
	void requestSchemaDoesNotExposeManagedFields() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath(
						"$.components.schemas.CreateInternshipApplicationRequest.properties.id"
				).doesNotExist())
				.andExpect(jsonPath(
						"$.components.schemas.CreateInternshipApplicationRequest.properties.createdAt"
				).doesNotExist())
				.andExpect(jsonPath(
						"$.components.schemas.CreateInternshipApplicationRequest.properties.updatedAt"
				).doesNotExist());
	}

	@Test
	void responseSchemaContainsManagedFields() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath(
						"$.components.schemas.InternshipApplicationResponse.properties.id"
				).exists())
				.andExpect(jsonPath(
						"$.components.schemas.InternshipApplicationResponse.properties.createdAt"
				).exists())
				.andExpect(jsonPath(
						"$.components.schemas.InternshipApplicationResponse.properties.updatedAt"
				).exists());
	}

	@Test
	void swaggerUiRouteIsAvailable() throws Exception {
		mockMvc.perform(get("/swagger-ui.html"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/swagger-ui/index.html"));
	}
}
