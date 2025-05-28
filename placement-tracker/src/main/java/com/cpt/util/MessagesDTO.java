package com.cpt.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class MessagesDTO {

	@JsonProperty("NoStds")
	private String studentFail;

	@JsonProperty("endDateError")
	private String endDateError;

	@JsonProperty("Nostudents")
	private String Nostudents;

	@JsonProperty("ResumeUploaded")
	private String ResumeUploaded;

	@JsonProperty("errorMessage")
	private String error;

	private String addPD;

	public String getAddPD() {
		return addPD;
	}

	public void setAddPD(String addPD) {
		this.addPD = addPD;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getResumeUploaded() {
		return ResumeUploaded;
	}

	public void setResumeUploaded(String resumeUploaded) {
		ResumeUploaded = resumeUploaded;
	}

	public String getNostudents() {
		return Nostudents;
	}

	public void setNostudents(String nostudents) {
		Nostudents = nostudents;
	}

	public String getEndDateError() {
		return endDateError;
	}

	public void setEndDateError(String endDateError) {
		this.endDateError = endDateError;
	}

	public String getStudentFail() {
		return studentFail;
	}

	public void setStudentFail(String studentFail) {
		this.studentFail = studentFail;
	}
}
