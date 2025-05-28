package com.cpt.model;

import java.util.List;

public class DriveStatsDTO {
	private DriveInfo drive;
	private int roundCount;
	private List<Parameter> parameters;
	private List<Criterion> criteria;
	private List<RoundShortlist> shortlistedPerRound;
	private int finalizedCount;
	private List<StudentShortlist> finalizedStudents;

	// Getters and setters
	public DriveInfo getDrive() {
		return drive;
	}

	public void setDrive(DriveInfo drive) {
		this.drive = drive;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public List<StudentShortlist> getFinalizedStudents() {
		return finalizedStudents;
	}

	public void setFinalizedStudents(List<StudentShortlist> finalizedStudents) {
		this.finalizedStudents = finalizedStudents;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

	public List<RoundShortlist> getShortlistedPerRound() {
		return shortlistedPerRound;
	}

	public void setShortlistedPerRound(List<RoundShortlist> shortlistedPerRound) {
		this.shortlistedPerRound = shortlistedPerRound;
	}

	public int getFinalizedCount() {
		return finalizedCount;
	}

	public void setFinalizedCount(int finalizedCount) {
		this.finalizedCount = finalizedCount;
	}
}