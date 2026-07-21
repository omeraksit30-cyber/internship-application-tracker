package com.omeraksit.internshiptracker.service;

import java.util.List;

import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.exception.ApplicationNotFoundException;
import com.omeraksit.internshiptracker.repository.InternshipApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class InternshipApplicationService {

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

	public List<InternshipApplication> getAll() {
		return repository.findAll();
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
