package com.project.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Profile {
	private Short id;
	private Short userId;

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

	public Short getBranchId() {
		return branchId;
	}

	public void setBranchId(Short branchId) {
		this.branchId = branchId;
	}

	public Double getGpa() {
		return gpa;
	}

	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}

	public Integer getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(Integer backlogs) {
		this.backlogs = backlogs;
	}

	public LocalDate getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}

	private Short branchId;
	private Double gpa;
	private Integer backlogs;
	private LocalDate updated;
}