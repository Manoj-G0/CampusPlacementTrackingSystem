package com.cpt.model;

public class Student {
	private String rollNo;
	private String fullName;
	private Long clgId;
	private String gender;
	private Double cgpa;
	private Integer backlogs;
	private Long branchId;
	private String collegeEmail;
	private String status;
	private String collegeName;
	private String branchName;
	private String githubUrl;

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	private String skills;

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	@Override
	public String toString() {
		return "Student [rollNo=" + rollNo + ", fullName=" + fullName + ", clgId=" + clgId + ", gender=" + gender
				+ ", cgpa=" + cgpa + ", backlogs=" + backlogs + ", branchId=" + branchId + ", collegeEmail="
				+ collegeEmail + ", status=" + status + "]";
	}

	public Student() {
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getCgpa() {
		return cgpa;
	}

	public void setCgpa(Double cgpa) {
		this.cgpa = cgpa;
	}

	public Integer getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(Integer backlogs) {
		this.backlogs = backlogs;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getCollegeEmail() {
		return collegeEmail;
	}

	public void setCollegeEmail(String collegeEmail) {
		this.collegeEmail = collegeEmail;
	}

	public Long getClgId() {
		return clgId;
	}

	public void setClgId(Long clgId) {
		this.clgId = clgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
