package com.project.model;

import lombok.Data;

@Data
public class Evaluation {
	private Short id;
	private Short applicationId;
	private Short phaseId;
	private Double score;
	private String comments;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Short applicationId) {
		this.applicationId = applicationId;
	}

	public Short getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Short phaseId) {
		this.phaseId = phaseId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}