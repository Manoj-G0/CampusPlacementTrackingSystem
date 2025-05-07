package com.project.model;

import lombok.Data;

@Data
public class HiringPhase {
	private Short id;
	private Short placementDriveId;
	private String name;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getPlacementDriveId() {
		return placementDriveId;
	}

	public void setPlacementDriveId(Short placementDriveId) {
		this.placementDriveId = placementDriveId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	private Integer sequence;
}