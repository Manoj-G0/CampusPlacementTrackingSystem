package com.cpt.model;

import java.util.List;
import java.util.Map;

public class RoundStats {
	private int totalStudents;
	private List<Map<String, Object>> phaseDetails;

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}

	public List<Map<String, Object>> getPhaseDetails() {
		return phaseDetails;
	}

	public void setPhaseDetails(List<Map<String, Object>> phaseDetails) {
		this.phaseDetails = phaseDetails;
	}
}
