package com.cpt.model;

public class Admin {
	private String adminId;
	private String adminName;
	private String designation;
	private int collegeId;
	private String email;

	// Constructor
	public Admin(String adminId, String adminName, String designation, String email, int collegeId) {
		this.adminId = adminId;
		this.adminName = adminName;
		this.designation = designation;
		this.email = email;
		this.collegeId = collegeId;
	}

	// Getters and Setters
	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(int collegeId) {
		this.collegeId = collegeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}