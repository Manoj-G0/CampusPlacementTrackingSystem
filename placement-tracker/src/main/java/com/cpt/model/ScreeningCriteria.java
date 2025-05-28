package com.cpt.model;

public class ScreeningCriteria {

	// Screening Criteria ID (Primary Key)
	private int scr_pld_id; // Placement Drive ID (Foreign Key reference)
	private double minCgpa; // Minimum CGPA required
	private int branch; // Branch requirement (e.g., "CSE", "ECE", etc.)
	private int backlogs;
	private String gender;

	public int getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(int backlogs) {
		this.backlogs = backlogs;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	// Default constructor
	public ScreeningCriteria() {
	}

	// Parameterized constructor

	// Getters and Setters

	public double getMinCgpa() {
		return minCgpa;
	}

	public int getScr_pld_id() {
		return scr_pld_id;
	}

	public void setScr_pld_id(int scr_pld_id) {
		this.scr_pld_id = scr_pld_id;
	}

	public void setMinCgpa(double minCgpa) {
		this.minCgpa = minCgpa;
	}

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}
}
