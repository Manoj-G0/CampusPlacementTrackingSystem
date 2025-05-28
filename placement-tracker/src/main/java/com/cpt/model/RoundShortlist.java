package com.cpt.model;

import java.util.List;

public class RoundShortlist {
	private int hphId;
	private String hphName;
	private int sequence;
	private int shortlistedCount;
	private List<StudentShortlist> students;

	public List<StudentShortlist> getStudents() {
		return students;
	}

	public void setStudents(List<StudentShortlist> students) {
		this.students = students;
	}

	// Getters and setters
	public int getHphId() {
		return hphId;
	}

	public void setHphId(int hphId) {
		this.hphId = hphId;
	}

	public String getHphName() {
		return hphName;
	}

	public void setHphName(String hphName) {
		this.hphName = hphName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getShortlistedCount() {
		return shortlistedCount;
	}

	public void setShortlistedCount(int shortlistedCount) {
		this.shortlistedCount = shortlistedCount;
	}
}