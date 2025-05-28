package com.cpt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiringPhase {
	private int hphId;
	private int hphPldId;
	private String hphName;
	private int hphSequence;
	private List<Parameter> parameters = new ArrayList<>();
	// private double score;

	// Getters and setters

	// public double getScore() {
	// return score;
	// }

	// public void setScore(double score) {
	// this.score = score;
	// }
	private String hphParameter;
	private double totalScore;
	private double thresholdScore;
	Map<String, Integer> hm = new HashMap<>();

	public String getHphParameter() {
		return hphParameter;
	}

	public void setHphParameter(String hphParameter) {
		this.hphParameter = hphParameter;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public double getThresholdScore() {
		return thresholdScore;
	}

	public void setThresholdScore(double thresholdScore) {
		this.thresholdScore = thresholdScore;
	}

	public Map<String, Integer> getHm() {
		return hm;
	}

	public void setHm(Map<String, Integer> hm) {
		this.hm = hm;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public int getHphPldId() {
		return hphPldId;
	}

	public void setHphPldId(int hphPldId) {
		this.hphPldId = hphPldId;
	}

	public String getHphName() {
		return hphName;
	}

	public void setHphName(String hphName) {
		this.hphName = hphName;
	}

	public int getHphSequence() {
		return hphSequence;
	}

	public void setHphSequence(int hphSequence) {
		this.hphSequence = hphSequence;
	}

	public int getHphId() {
		return hphId;
	}

	public void setHphId(int hphId) {
		this.hphId = hphId;
	}
}
