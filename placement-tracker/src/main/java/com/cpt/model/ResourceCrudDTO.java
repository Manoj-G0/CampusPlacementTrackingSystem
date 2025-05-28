package com.cpt.model;

public class ResourceCrudDTO {

	@Override
	public String toString() {
		return "ResourceCrudDTO [resourceId=" + resourceId + ", clgName=" + clgName + ", brnName=" + brnName
				+ ", resourceCapacity=" + resourceCapacity + "]";
	}

	private int resourceId;
	private String clgName;
	private String brnName;
	private int resourceCapacity;

	// Getters and Setters
	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getClgName() {
		return clgName;
	}

	public void setClgName(String clgName) {
		this.clgName = clgName;
	}

	public String getBrnName() {
		return brnName;
	}

	public void setBrnName(String brnName) {
		this.brnName = brnName;
	}

	public int getResourceCapacity() {
		return resourceCapacity;
	}

	public void setResourceCapacity(int resourceCapacity) {
		this.resourceCapacity = resourceCapacity;
	}
}
