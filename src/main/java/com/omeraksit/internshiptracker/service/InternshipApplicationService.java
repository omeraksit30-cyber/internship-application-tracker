package com.omeraksit.internshiptracker.service;

import java.util.Set;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import com.omeraksit.internshiptracker.exception.ApplicationNotFoundException;
import com.omeraksit.internshiptracker.repository.InternshipApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class InternshipApplicationService {

	private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
			"id",
			"companyName",
			"positionTitle",
			"status",
			"workMode",
			"applicationDate",
			"followUpDate",
			"createdAt",
			"updatedAt"
	);

	private final InternshipApplicationRepository repository;

	public InternshipApplicationService(InternshipApplicationRepository repository) {
		this.repository = repository;
	}

	public InternshipApplication create(InternshipApplication application) {
		if (application == null) {
			throw new IllegalArgumentException("Application must not be null");
		}

		application.setId(null);
		return repository.save(application);
	}

	public Page<InternshipApplication> searchApplications(
			ApplicationStatus status,
			WorkMode workMode,
			String search,
			int page,
			int size,
			String sortBy,
			String direction) {
		if (page < 0) {
			throw new IllegalArgumentException("Page must be zero or greater");
		}
		if (size < 1) {
			throw new IllegalArgumentException("Size must be at least 1");
		}
		if (size > 100) {
			throw new IllegalArgumentException("Size must not exceed 100");
		}
		if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException("Unsupported sort field: " + sortBy);
		}
		if (direction == null
				|| (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))) {
			throw new IllegalArgumentException("Direction must be either asc or desc");
		}

		String normalizedSearch = null;
		if (search != null && !search.isBlank()) {
			normalizedSearch = search.trim();
			if (normalizedSearch.length() > 100) {
				throw new IllegalArgumentException("Search term must not exceed 100 characters");
			}
		}

		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		return repository.search(status, workMode, normalizedSearch, pageable);
	}

	public InternshipApplication getById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Application id must not be null");
		}

		return repository.findById(id)
				.orElseThrow(() -> new ApplicationNotFoundException(id));
	}

	public InternshipApplication update(Long id, InternshipApplication updatedApplication) {
		if (id == null) {
			throw new IllegalArgumentException("Application id must not be null");
		}
		if (updatedApplication == null) {
			throw new IllegalArgumentException("Updated application must not be null");
		}

		InternshipApplication existingApplication = repository.findById(id)
				.orElseThrow(() -> new ApplicationNotFoundException(id));

		existingApplication.setCompanyName(updatedApplication.getCompanyName());
		existingApplication.setPositionTitle(updatedApplication.getPositionTitle());
		existingApplication.setStatus(updatedApplication.getStatus());
		existingApplication.setWorkMode(updatedApplication.getWorkMode());
		existingApplication.setLocation(updatedApplication.getLocation());
		existingApplication.setApplicationDate(updatedApplication.getApplicationDate());
		existingApplication.setFollowUpDate(updatedApplication.getFollowUpDate());
		existingApplication.setContactName(updatedApplication.getContactName());
		existingApplication.setContactEmail(updatedApplication.getContactEmail());
		existingApplication.setJobUrl(updatedApplication.getJobUrl());
		existingApplication.setNotes(updatedApplication.getNotes());

		return repository.save(existingApplication);
	}

	public void delete(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Application id must not be null");
		}

		InternshipApplication existingApplication = repository.findById(id)
				.orElseThrow(() -> new ApplicationNotFoundException(id));

		repository.delete(existingApplication);
	}
}
