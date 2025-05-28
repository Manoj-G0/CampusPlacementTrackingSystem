package com.cpt.model;

import java.util.List;

public class CompanyStatsDTO {
	private Company company;
	private List<DriveStatsDTO> drives;

	// Getters and setters
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<DriveStatsDTO> getDrives() {
		return drives;
	}

	public void setDrives(List<DriveStatsDTO> drives) {
		this.drives = drives;
	}
}