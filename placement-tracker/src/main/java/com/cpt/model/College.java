package com.cpt.model;

public class College {
	private Integer clgId;
	private String clgName;
	private String clgAddress;
	private String clgContact;

	public College() {
	}

	public College(Integer clgId, String clgName) {

		this.clgId = clgId;
		this.clgName = clgName;
	}

	public Integer getClgId() {
		return clgId;
	}

	public void setClgId(Integer clgId) {
		this.clgId = clgId;
	}

	public String getClgName() {
		return clgName;
	}

	public void setClgName(String clgName) {
		this.clgName = clgName;
	}

	public String getClgAddress() {
		return clgAddress;
	}

	public void setClgAddress(String clgAddress) {
		this.clgAddress = clgAddress;
	}

	public String getClgContact() {
		return clgContact;
	}

	public void setClgContact(String clgContact) {
		this.clgContact = clgContact;
	}

	@Override
	public String toString() {
		return "College [clgId=" + clgId + ", clgName=" + clgName + ", clgAddress=" + clgAddress + ", clgContact="
				+ clgContact + "]";
	}

}
