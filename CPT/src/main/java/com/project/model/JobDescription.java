package com.project.model;

import lombok.Data;

@Data
public class JobDescription {
	private Short id;
	private Short companyId;
	private Short placementDriveId;
	private String role;
	private Double packageAmount;
	private Double minGpa;
	private Integer maxBacklogs;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Short companyId) {
		this.companyId = companyId;
	}

	public Short getPlacementDriveId() {
		return placementDriveId;
	}

	public void setPlacementDriveId(Short placementDriveId) {
		this.placementDriveId = placementDriveId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(Double packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Double getMinGpa() {
		return minGpa;
	}

	public void setMinGpa(Double minGpa) {
		this.minGpa = minGpa;
	}

	public Integer getMaxBacklogs() {
		return maxBacklogs;
	}

	public void setMaxBacklogs(Integer maxBacklogs) {
		this.maxBacklogs = maxBacklogs;
	}

	public String getAllowedBranches() {
		return allowedBranches;
	}

	public void setAllowedBranches(String allowedBranches) {
		this.allowedBranches = allowedBranches;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	private String allowedBranches;
	private String skills;
}