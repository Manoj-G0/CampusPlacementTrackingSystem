package com.cpt.model;

import org.springframework.stereotype.Component;

@Component
public class Resource {
	int resourceId;
	String college;
	String branch;
	int capacity;
	int clg_id;
	public int getClg_id() {
		return clg_id;
	}
	public void setClg_id(int clg_id) {
		this.clg_id = clg_id;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
