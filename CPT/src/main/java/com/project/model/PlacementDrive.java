package com.project.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PlacementDrive {
	private Short id;
	private Short collegeId;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;

	public PlacementDrive() {
		super();
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(Short collegeId) {
		this.collegeId = collegeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(Double packageAmount) {
		this.packageAmount = packageAmount;
	}

	private Double minGpa;
	private Integer maxBacklogs;
	private String allowedBranches;
	private Double packageAmount;
}
