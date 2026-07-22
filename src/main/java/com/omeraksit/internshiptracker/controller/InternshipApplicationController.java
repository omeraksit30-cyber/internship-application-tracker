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
	public ResponseEntity<InternshipApplicationResponse> create(
			@Valid @RequestBody CreateInternshipApplicationRequest request) {
		InternshipApplication entity = mapper.toEntity(request);
		InternshipApplication savedEntity = service.create(entity);
		InternshipApplicationResponse response = mapper.toResponse(savedEntity);
		URI location = URI.create(BASE_PATH + "/" + savedEntity.getId());

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping
	public ResponseEntity<PagedResponse<InternshipApplicationResponse>> getAll(
			@RequestParam(required = false) ApplicationStatus status,
			@RequestParam(required = false) WorkMode workMode,
			@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
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
	public ResponseEntity<InternshipApplicationResponse> getById(@PathVariable Long id) {
		InternshipApplication entity = service.getById(id);
		InternshipApplicationResponse response = mapper.toResponse(entity);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<InternshipApplicationResponse> update(
			@PathVariable Long id,
			@Valid @RequestBody UpdateInternshipApplicationRequest request) {
		InternshipApplication mappedEntity = mapper.toEntity(request);
		InternshipApplication updatedEntity = service.update(id, mappedEntity);
		InternshipApplicationResponse response = mapper.toResponse(updatedEntity);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}
