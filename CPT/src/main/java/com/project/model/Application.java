package com.project.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Application {
	private Short id;
	private Short userId;

	public Application() {
		super();
	}

	public Application(Short id, Short userId, Short placementDriveId, Short companyId, LocalDate applicationDate,
			String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.placementDriveId = placementDriveId;
		this.companyId = companyId;
		this.applicationDate = applicationDate;
		this.status = status;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getUserId() {
		return userId;
	}

	public void setUserId(Short userId) {
		this.userId = userId;
	}

	public Short getPlacementDriveId() {
		return placementDriveId;
	}

	public void setPlacementDriveId(Short placementDriveId) {
		this.placementDriveId = placementDriveId;
	}

	public Short getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Short companyId) {
		this.companyId = companyId;
	}

	public LocalDate getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private Short placementDriveId;
	private Short companyId;
	private LocalDate applicationDate;
	private String status; // PEND, APPR, REJC
}