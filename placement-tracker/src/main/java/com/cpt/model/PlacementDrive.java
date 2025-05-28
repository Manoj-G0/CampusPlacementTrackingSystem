package com.cpt.model;

import java.sql.Date;

public class PlacementDrive {
	private Integer pldId;
	private Integer collegeId;
	private Integer companyId;
	private String name;
	private Date startDate;
	private Date endDate;
	private String pldJd; // Job description
	private String pldRole; // e.g., Software Engineer
	private Double pldSalary;
	private String status;
	private Company company;
	private College college;

	public PlacementDrive() {
		this.status = "NOT ASSIGNED";
	}

	public PlacementDrive(int Id, String Name) {
		// TODO Auto-generated constructor stub
		this.pldId = Id;
		this.name = Name;
	}

	public String getPldJd() {
		return pldJd;
	}

	public void setPldJd(String pldJd) {
		this.pldJd = pldJd;
	}

	public String getPldRole() {
		return pldRole;
	}

	public void setPldRole(String pldRole) {
		this.pldRole = pldRole;
	}

	public Double getPldSalary() {
		return pldSalary;
	}

	public void setPldSalary(Double pldSalary) {
		this.pldSalary = pldSalary;
	}

	public Integer getPldId() {
		return pldId;
	}

	public void setPldId(Integer pldId) {
		this.pldId = pldId;
	}

	public Integer getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(Integer collegeId) {
		this.collegeId = collegeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "PlacementDrive [pldId=" + pldId + ", collegeId=" + collegeId + ", companyId=" + companyId + ", name="
				+ name + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status + ", company="
				+ company + ", college=" + college + "]";
	}

}
