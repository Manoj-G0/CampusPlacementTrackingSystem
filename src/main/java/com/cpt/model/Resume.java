package com.cpt.model;

import java.time.LocalDate;

public class Resume {
	private Integer resId;
	private String resUsrId;
	private String resFile;
	private LocalDate resUploadDate;

	// Getters and Setters
	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	public String getResUsrId() {
		return resUsrId;
	}

	public void setResUsrId(String resUsrId) {
		this.resUsrId = resUsrId;
	}

	public String getResFile() {
		return resFile;
	}

	public void setResFile(String resFile) {
		this.resFile = resFile;
	}

	public LocalDate getResUploadDate() {
		return resUploadDate;
	}

	public void setResUploadDate(LocalDate resUploadDate) {
		this.resUploadDate = resUploadDate;
	}
}