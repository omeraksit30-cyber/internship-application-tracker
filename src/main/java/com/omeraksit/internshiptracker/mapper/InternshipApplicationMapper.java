package com.omeraksit.internshiptracker.mapper;

import java.util.ArrayList;
import java.util.List;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.dto.request.CreateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.request.UpdateInternshipApplicationRequest;
import com.omeraksit.internshiptracker.dto.response.InternshipApplicationResponse;
import com.omeraksit.internshiptracker.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class InternshipApplicationMapper {

	public InternshipApplication toEntity(CreateInternshipApplicationRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Create request must not be null");
		}

		InternshipApplication entity = new InternshipApplication();
		entity.setCompanyName(request.getCompanyName());
		entity.setPositionTitle(request.getPositionTitle());
		entity.setStatus(
				request.getStatus() == null ? ApplicationStatus.PLANNED : request.getStatus()
		);
		entity.setWorkMode(request.getWorkMode());
		entity.setLocation(request.getLocation());
		entity.setApplicationDate(request.getApplicationDate());
		entity.setFollowUpDate(request.getFollowUpDate());
		entity.setContactName(request.getContactName());
		entity.setContactEmail(request.getContactEmail());
		entity.setJobUrl(request.getJobUrl());
		entity.setNotes(request.getNotes());
		return entity;
	}

	public InternshipApplication toEntity(UpdateInternshipApplicationRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("Update request must not be null");
		}

		InternshipApplication entity = new InternshipApplication();
		entity.setCompanyName(request.getCompanyName());
		entity.setPositionTitle(request.getPositionTitle());
		entity.setStatus(request.getStatus());
		entity.setWorkMode(request.getWorkMode());
		entity.setLocation(request.getLocation());
		entity.setApplicationDate(request.getApplicationDate());
		entity.setFollowUpDate(request.getFollowUpDate());
		entity.setContactName(request.getContactName());
		entity.setContactEmail(request.getContactEmail());
		entity.setJobUrl(request.getJobUrl());
		entity.setNotes(request.getNotes());
		return entity;
	}

	public InternshipApplicationResponse toResponse(InternshipApplication entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity must not be null");
		}

		return new InternshipApplicationResponse(
				entity.getId(),
				entity.getCompanyName(),
				entity.getPositionTitle(),
				entity.getStatus(),
				entity.getWorkMode(),
				entity.getLocation(),
				entity.getApplicationDate(),
				entity.getFollowUpDate(),
				entity.getContactName(),
				entity.getContactEmail(),
				entity.getJobUrl(),
				entity.getNotes(),
				entity.getCreatedAt(),
				entity.getUpdatedAt()
		);
	}

	public List<InternshipApplicationResponse> toResponseList(
			List<InternshipApplication> entities) {
		if (entities == null) {
			throw new IllegalArgumentException("Entity list must not be null");
		}

		List<InternshipApplicationResponse> responses = new ArrayList<>();
		for (InternshipApplication entity : entities) {
			responses.add(toResponse(entity));
		}
		return responses;
	}

	public PagedResponse<InternshipApplicationResponse> toPagedResponse(
			Page<InternshipApplication> entityPage) {
		if (entityPage == null) {
			throw new IllegalArgumentException("Entity page must not be null");
		}

		List<InternshipApplicationResponse> content = toResponseList(entityPage.getContent());
		return new PagedResponse<>(
				content,
				entityPage.getNumber(),
				entityPage.getSize(),
				entityPage.getTotalElements(),
				entityPage.getTotalPages(),
				entityPage.getNumberOfElements(),
				entityPage.isFirst(),
				entityPage.isLast(),
				entityPage.isEmpty()
		);
	}
}
