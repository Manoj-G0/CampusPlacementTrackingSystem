package com.project.model;

import lombok.Data;

@Data
public class Branch {
	private Short id;
	private Short collegeId;
	private String name;
	private String description;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(Short collegeId) {
		this.collegeId = collegeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}