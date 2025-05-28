package com.cpt.model;

public class Shortlisted {
	private String studentId; // student_id (roll number)
	private String studentName; // student name
	private double score; // score for the final phase
	private String status;
	private int placementId;
	private String email;
	private String remarks;
	public int getPlacementId() {
		return placementId;
	}

	public void setPlacementId(int placementId) {
		this.placementId = placementId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Getters and setters
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Shortlisted [studentId=" + studentId + ", studentName=" + studentName + ", score=" + score + ", status="
				+ status + ", getStudentId()=" + getStudentId() + ", getStudentName()=" + getStudentName()
				+ ", getScore()=" + getScore() + ", getStatus()=" + getStatus() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
