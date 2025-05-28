package com.cpt.model;

public class Criterion {
	private int scrId;
	private double minGpa;
	private int minBacklogs;
	private String branchName;

	// Getters and setters
	public int getScrId() {
		return scrId;
	}

	public void setScrId(int scrId) {
		this.scrId = scrId;
	}

	public double getMinGpa() {
		return minGpa;
	}

	public void setMinGpa(double minGpa) {
		this.minGpa = minGpa;
	}

	public Criterion() {
		super();
	}

	public int getMinBacklogs() {
		return minBacklogs;
	}

	public void setMinBacklogs(int minBacklogs) {
		this.minBacklogs = minBacklogs;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}