package com.project.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ResourceAllocation {
	private Short id;
	private Short placementDriveId;
	private String type;
	private Integer quantity;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	private LocalDate date;
}