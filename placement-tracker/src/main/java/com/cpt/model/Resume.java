package com.cpt.model;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class Resume {
	private Integer resId; // Primary key
	private String resUsrId; // References users(usr_id), matches PollNo
	private String resFileName; // e.g., s3://resumes/21s91a0501_resume.pdf
	private byte[] resumeData;
	private String resType;

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public byte[] getResumeData() {
		return resumeData;
	}

	public void setResumeData(byte[] resumeData) {
		this.resumeData = resumeData;
	}

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

	public LocalDate getResUploadDate() {
		return resUploadDate;
	}

	public void setResUploadDate(LocalDate resUploadDate) {
		this.resUploadDate = resUploadDate;
	}
}