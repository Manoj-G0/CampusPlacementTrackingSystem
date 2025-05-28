package com.cpt.model;

import java.util.Arrays;

public class Profile {
	private Long prfId;
	private String prfUsrId;
	private String prfBrnName;
	private String name;
	private double prfGpa;
	private java.sql.Date prfUpdated;
	private byte[] prfImage;
	private String prfContact;
	private String skills;
	private String githubUrl;
	private String desg;
	private String email;
	private String gender;
	private int backlogs;
	private String branchName;
	private String collegeName;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public int getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(int backlogs) {
		this.backlogs = backlogs;
	}

	@Override
	public String toString() {
		return "Profile [prfId=" + prfId + ", prfUsrId=" + prfUsrId + ", prfBrnName=" + prfBrnName + ", name=" + name
				+ ", prfGpa=" + prfGpa + ", prfUpdated=" + prfUpdated + ", prfImage=" + Arrays.toString(prfImage)
				+ ", prfContact=" + prfContact + ", skills=" + skills + ", githubUrl=" + githubUrl + ", desg=" + desg
				+ ", email=" + email + ", company=" + company + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPrfImage() {
		return prfImage;
	}

	public String getPrfBrnName() {
		return prfBrnName;
	}

	public void setPrfBrnName(String prfBrnName) {
		this.prfBrnName = prfBrnName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesg() {
		return desg;
	}

	public void setDesg(String desg) {
		this.desg = desg;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setPrfImage(byte[] prfImage) {
		this.prfImage = prfImage;
	}

	private String company;

	public Long getPrfId() {
		return prfId;
	}

	public void setPrfId(Long prfId) {
		this.prfId = prfId;
	}

	public String getPrfUsrId() {
		return prfUsrId;
	}

	public void setPrfUsrId(String prfUsrId) {
		this.prfUsrId = prfUsrId;
	}

	public double getPrfGpa() {
		return prfGpa;
	}

	public void setPrfGpa(double prfGpa) {
		this.prfGpa = prfGpa;
	}

	public java.sql.Date getPrfUpdated() {
		return prfUpdated;
	}

	public void setPrfUpdated(java.sql.Date prfUpdated) {
		this.prfUpdated = prfUpdated;
	}

	public String getPrfContact() {
		return prfContact;
	}

	public void setPrfContact(String prfContact) {
		this.prfContact = prfContact;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}