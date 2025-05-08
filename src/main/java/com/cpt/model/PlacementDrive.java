package com.cpt.model;

import java.time.LocalDate;

public class PlacementDrive {
	private Integer pldId;
	private Integer pldClgId;
	private Integer pldCmpId;
	private String pldName;
	private LocalDate pldStartDate;
	private LocalDate pldEndDate;
	private String pldStatus;
	private String cmpName;

	// Getters and Setters
	public Integer getPldId() {
		return pldId;
	}

	public void setPldId(Integer pldId) {
		this.pldId = pldId;
	}

	public Integer getPldClgId() {
		return pldClgId;
	}

	public void setPldClgId(Integer pldClgId) {
		this.pldClgId = pldClgId;
	}

	public Integer getPldCmpId() {
		return pldCmpId;
	}

	public void setPldCmpId(Integer pldCmpId) {
		this.pldCmpId = pldCmpId;
	}

	public String getPldName() {
		return pldName;
	}

	public void setPldName(String pldName) {
		this.pldName = pldName;
	}

	public LocalDate getPldStartDate() {
		return pldStartDate;
	}

	public void setPldStartDate(LocalDate pldStartDate) {
		this.pldStartDate = pldStartDate;
	}

	public LocalDate getPldEndDate() {
		return pldEndDate;
	}

	public void setPldEndDate(LocalDate pldEndDate) {
		this.pldEndDate = pldEndDate;
	}

	public String getPldStatus() {
		return pldStatus;
	}

	public void setPldStatus(String pldStatus) {
		this.pldStatus = pldStatus;
	}

	public String getCmpName() {
		return cmpName;
	}

	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}
}