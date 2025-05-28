package com.cpt.model;
public class ApplicationDTO {
    private Long appId;
    private String appUsrId; // User ID (roll_no)
    private Integer appPldId; // Placement Drive ID
    private String applicationStatus; // 'Approved', 'Rejected', or 'Pending'
    private String fullName;  // Student's full name
    private String branchId;  // Student's branch
    private Double cgpa;      // Student's CGPA
    private String status;    // Student's status (Active/Inactive)
    private String collegeEmail; // Student's college email
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public String getAppUsrId() {
		return appUsrId;
	}
	public void setAppUsrId(String appUsrId) {
		this.appUsrId = appUsrId;
	}
	public Integer getAppPldId() {
		return appPldId;
	}
	public void setAppPldId(Integer appPldId) {
		this.appPldId = appPldId;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Double getCgpa() {
		return cgpa;
	}
	public void setCgpa(Double cgpa) {
		this.cgpa = cgpa;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCollegeEmail() {
		return collegeEmail;
	}
	public void setCollegeEmail(String collegeEmail) {
		this.collegeEmail = collegeEmail;
	}

    // Getters and Setters
}
