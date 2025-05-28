package com.cpt.model;

import java.util.List;

public class StudentStatsDTO {
	private Student student;
	private int attendedDrivesCount;
	private int selectedCount;
	private int eligibleDrivesCount;
	private List<DriveInfo> attendedDrives;
	private List<DriveInfo> selectedDrives;
	private List<DriveInfo> eligibleDrives;

	// Getters and Setters
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getAttendedDrivesCount() {
		return attendedDrivesCount;
	}

	public void setAttendedDrivesCount(int count) {
		this.attendedDrivesCount = count;
	}

	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int count) {
		this.selectedCount = count;
	}

	public int getEligibleDrivesCount() {
		return eligibleDrivesCount;
	}

	public void setEligibleDrivesCount(int count) {
		this.eligibleDrivesCount = count;
	}

	public List<DriveInfo> getAttendedDrives() {
		return attendedDrives;
	}

	public void setAttendedDrives(List<DriveInfo> drives) {
		this.attendedDrives = drives;
	}

	public List<DriveInfo> getSelectedDrives() {
		return selectedDrives;
	}

	public void setSelectedDrives(List<DriveInfo> drives) {
		this.selectedDrives = drives;
	}

	public List<DriveInfo> getEligibleDrives() {
		return eligibleDrives;
	}

	public void setEligibleDrives(List<DriveInfo> drives) {
		this.eligibleDrives = drives;
	}
}