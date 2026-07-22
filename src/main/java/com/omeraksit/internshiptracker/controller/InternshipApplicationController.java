package com.omeraksit.internshiptracker.controller;

import java.net.URI;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.dto.request.CreateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.request.UpdateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.response.InternshipApplicationResponse;
import com.omeraksit.internshiptracker.dto.response.PagedResponse;
import com.omeraksit.internshiptracker.mapper.InternshipApplicationMapper;
import com.omeraksit.internshiptracker.service.InternshipApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(InternshipApplicationController.BASE_PATH)
@Tag(
		name = "Internship Applications",
		description = "Create, view, update, delete, filter and search internship applications."
)
public class InternshipApplicationController {

	static final String BASE_PATH = "/api/applications";

	private final InternshipApplicationService service;
	private final InternshipApplicationMapper mapper;

	public InternshipApplicationController(
			InternshipApplicationService service,
			InternshipApplicationMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@PostMapping
	@Operation(
			summary = "Create an internship application",
			description = "Creates a new internship application from the validated request body."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Internship application created"),
		@ApiResponse(responseCode = "400", description = "Invalid request or validation failure"),
		@ApiResponse(responseCode = "500", description = "Unexpected server error")
	})
	public ResponseEntity<InternshipApplicationResponse> create(
			@Valid @RequestBody CreateInternshipApplicationRequest request) {
		InternshipApplication entity = mapper.toEntity(request);
		InternshipApplication savedEntity = service.create(entity);
		InternshipApplicationResponse response = mapper.toResponse(savedEntity);
		URI location = URI.create(BASE_PATH + "/" + savedEntity.getId());

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping
	@Operation(
			summary = "Search internship applications",
			description = "Returns a paginated list that can be filtered by status and work mode, "
					+ "searched by company or position, and safely sorted."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Paginated application response"),
		@ApiResponse(
				responseCode = "400",
				description = "Invalid filter, pagination or sorting parameter"
		),
		@ApiResponse(responseCode = "500", description = "Unexpected server error")
	})
	public ResponseEntity<PagedResponse<InternshipApplicationResponse>> getAll(
			@Parameter(description = "Optional application status filter")
			@RequestParam(required = false) ApplicationStatus status,
			@Parameter(description = "Optional work mode filter")
			@RequestParam(required = false) WorkMode workMode,
			@Parameter(
					description = "Optional case-insensitive search in company and position names. "
							+ "Maximum 100 characters."
			)
			@RequestParam(required = false) String search,
			@Parameter(description = "Zero-based page number")
			@RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Page size between 1 and 100")
			@RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Supported sort field")
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@Parameter(description = "Sort direction: asc or desc")
			@RequestParam(defaultValue = "desc") String direction) {
		Page<InternshipApplication> entityPage = service.searchApplications(
				status,
				workMode,
				search,
				page,
				size,
				sortBy,
				direction
		);
		PagedResponse<InternshipApplicationResponse> response =
				mapper.toPagedResponse(entityPage);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get an internship application by ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Internship application found"),
		@ApiResponse(responseCode = "400", description = "Invalid ID"),
		@ApiResponse(responseCode = "404", description = "Internship application not found"),
		@ApiResponse(responseCode = "500", description = "Unexpected server error")
	})
	public ResponseEntity<InternshipApplicationResponse> getById(@PathVariable Long id) {
		InternshipApplication entity = service.getById(id);
		InternshipApplicationResponse response = mapper.toResponse(entity);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	@Operation(
			summary = "Update an internship application",
			description = "Completely updates the editable fields of an existing application."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Internship application updated"),
		@ApiResponse(responseCode = "400", description = "Invalid request or validation failure"),
		@ApiResponse(responseCode = "404", description = "Internship application not found"),
		@ApiResponse(responseCode = "500", description = "Unexpected server error")
	})
	public ResponseEntity<InternshipApplicationResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody UpdateInternshipApplicationRequest request) {
		InternshipApplication mappedEntity = mapper.toEntity(request);
		InternshipApplication updatedEntity = service.update(id, mappedEntity);
		InternshipApplicationResponse response = mapper.toResponse(updatedEntity);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an internship application")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Internship application deleted"),
		@ApiResponse(responseCode = "400", description = "Invalid ID"),
		@ApiResponse(responseCode = "404", description = "Internship application not found"),
		@ApiResponse(responseCode = "500", description = "Unexpected server error")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}
