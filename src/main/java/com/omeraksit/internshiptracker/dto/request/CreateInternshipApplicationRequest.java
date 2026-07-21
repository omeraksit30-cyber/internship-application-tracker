package com.omeraksit.internshiptracker.dto.request;

import java.time.LocalDate;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.WorkMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateInternshipApplicationRequest {

	@NotBlank(message = "Company name is required")
	@Size(max = 150, message = "Company name must not exceed 150 characters")
	private String companyName;

	@NotBlank(message = "Position title is required")
	@Size(max = 150, message = "Position title must not exceed 150 characters")
	private String positionTitle;

	private ApplicationStatus status;

	private WorkMode workMode;

	@Size(max = 150, message = "Location must not exceed 150 characters")
	private String location;

	private LocalDate applicationDate;

	private LocalDate followUpDate;

	@Size(max = 120, message = "Contact name must not exceed 120 characters")
	private String contactName;

	@Email(message = "Contact email must be valid")
	@Size(max = 255, message = "Contact email must not exceed 255 characters")
	private String contactEmail;

	@Size(max = 500, message = "Job URL must not exceed 500 characters")
	private String jobUrl;

	@Size(max = 3000, message = "Notes must not exceed 3000 characters")
	private String notes;

	public CreateInternshipApplicationRequest() {
	}

	public CreateInternshipApplicationRequest(
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
			String notes) {
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
}
