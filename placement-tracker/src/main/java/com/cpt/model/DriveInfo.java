package com.cpt.model;

public class DriveInfo {
	private int pldId;
	private String pldName;
	private String cmpName;
	private String status; // For attended/selected drives
	private String pldDate;
	private double minCgpa; // For eligible drives
	private int maxBacklogs; // For eligible drives
	private String startDate;
	private String roleName;
	private double packageAmount;

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	private String collegeName;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public double getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(double packageAmount) {
		this.packageAmount = packageAmount;
	}

	// Getters and Setters
	public int getPldId() {
		return pldId;
	}

	public void setPldId(int pldId) {
		this.pldId = pldId;
	}

	public String getPldName() {
		return pldName;
	}

	public void setPldName(String pldName) {
		this.pldName = pldName;
	}

	public String getCmpName() {
		return cmpName;
	}

	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPldDate() {
		return pldDate;
	}

	public void setPldDate(String pldDate) {
		this.pldDate = pldDate;
	}

	public double getMinCgpa() {
		return minCgpa;
	}

	public void setMinCgpa(double minCgpa) {
		this.minCgpa = minCgpa;
	}

	public int getMaxBacklogs() {
		return maxBacklogs;
	}

	public void setMaxBacklogs(int maxBacklogs) {
		this.maxBacklogs = maxBacklogs;
	}
}