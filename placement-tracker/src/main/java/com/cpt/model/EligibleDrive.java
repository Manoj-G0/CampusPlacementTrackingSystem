package com.cpt.model;

import java.time.LocalDate;

public class EligibleDrive {
	private int eligibleId;
	private String userId;
	private int pldId;
	private LocalDate endDate;
	private String companyName;
	private String companyDescription;
	private String driveName;
	private LocalDate startDate;

	public int getPldId() {
		return pldId;
	}

	public void setPldId(int pldId) {
		this.pldId = pldId;
	}

	public int getEligibleId() {
		return eligibleId;
	}

	public void setEligibleId(int eligibleId) {
		this.eligibleId = eligibleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDriveName() {
		return driveName;
	}

	public void setDriveName(String driveName) {
		this.driveName = driveName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDescription() {
		return companyDescription;
	}

	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}

	// Getters and setters
}
