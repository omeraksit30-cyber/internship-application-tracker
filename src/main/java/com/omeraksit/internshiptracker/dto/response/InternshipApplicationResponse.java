package com.omeraksit.internshiptracker.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.WorkMode;
import io.swagger.v3.oas.annotations.media.Schema;

public class InternshipApplicationResponse {

	@Schema(description = "Database-generated application ID", accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;
	private String companyName;
	private String positionTitle;
	private ApplicationStatus status;
	private WorkMode workMode;
	private String location;
	private LocalDate applicationDate;
	private LocalDate followUpDate;
	private String contactName;
	private String contactEmail;
	private String jobUrl;
	private String notes;
	@Schema(description = "Date and time when the application was created", accessMode = Schema.AccessMode.READ_ONLY)
	private LocalDateTime createdAt;

	@Schema(description = "Date and time when the application was last updated", accessMode = Schema.AccessMode.READ_ONLY)
	private LocalDateTime updatedAt;

	public InternshipApplicationResponse() {
	}

	public InternshipApplicationResponse(
			Long id,
			String companyName,
			String positionTitle,
			ApplicationStatus status,
			WorkMode workMode,
			String location,
			LocalDate applicationDate,
			LocalDate followUpDate,
			String contactName,
			String contactEmail,
			String jobUrl,
			String notes,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.companyName = companyName;
		this.positionTitle = positionTitle;
		this.status = status;
		this.workMode = workMode;
		this.location = location;
		this.applicationDate = applicationDate;
		this.followUpDate = followUpDate;
		this.contactName = contactName;
		this.contactEmail = contactEmail;
		this.jobUrl = jobUrl;
		this.notes = notes;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public WorkMode getWorkMode() {
		return workMode;
	}

	public void setWorkMode(WorkMode workMode) {
		this.workMode = workMode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}

	public LocalDate getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(LocalDate followUpDate) {
		this.followUpDate = followUpDate;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
