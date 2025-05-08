package com.cpt.model;

public class User {
	private String usrId; // e.g., 21s91a0501, poid12321322, hrinfosys
	private String usrPassword;
	private String usrRole; // STUD, ADMIN, HR

	// Getters and Setters
	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getUsrPassword() {
		return usrPassword;
	}

	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}

	public String getUsrRole() {
		return usrRole;
	}

	public void setUsrRole(String usrRole) {
		this.usrRole = usrRole;
	}
}