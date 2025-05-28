package com.cpt.model;

import java.util.List;

public class StudentList {
	private List<Student> students;

	// Default constructor for Jackson
	public StudentList() {
	}

	public StudentList(List<Student> students) {
		this.students = students;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
}