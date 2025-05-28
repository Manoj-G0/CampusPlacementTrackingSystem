package com.cpt.model;

public class RoundParameter {
	private String phaseName;
	private String parameter;
	private int score;
	private double phaseThreshold;
	private double paramThreshold;

	// Getters and Setters
	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public double getPhaseThreshold() {
		return phaseThreshold;
	}

	public void setPhaseThreshold(double phaseThreshold) {
		this.phaseThreshold = phaseThreshold;
	}

	public double getParamThreshold() {
		return paramThreshold;
	}

	public void setParamThreshold(double paramThreshold) {
		this.paramThreshold = paramThreshold;
	}
}
