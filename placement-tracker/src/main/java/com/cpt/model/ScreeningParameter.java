package com.cpt.model;

public class ScreeningParameter {
	private Integer paramId;
	private Integer roundId;
	private String paramName;
	private Double thresholdScore;
	private Double totalScore;

	// Getters and Setters
	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Double getThresholdScore() {
		return thresholdScore;
	}

	public void setThresholdScore(Double thresholdScore) {
		this.thresholdScore = thresholdScore;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
}