package com.cpt.model;

public class RoundWiseShortlisted {
	private String phase_name;
	private String studentId; // student_id (roll number)
	private String student_name; // student name
	private double score;
	private int phaseId;
	private int placementId;// score for the round

	// Getters and setters

	public int getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(int phaseId) {
		this.phaseId = phaseId;
	}

	public int getPlacementId() {
		return placementId;
	}

	public void setPlacementId(int placementId) {
		this.placementId = placementId;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getPhase_name() {
		return phase_name;
	}

	public void setPhase_name(String phase_name) {
		this.phase_name = phase_name;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
