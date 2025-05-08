package com.cpt.model;

import java.time.LocalDate;

public class Notification {
	private Integer ntfId;
	private String ntfUsrId;
	private String ntfMessage;
	private LocalDate ntfDate;
	private Boolean ntfRead;

	// Getters and Setters
	public Integer getNtfId() {
		return ntfId;
	}

	public void setNtfId(Integer ntfId) {
		this.ntfId = ntfId;
	}

	public String getNtfUsrId() {
		return ntfUsrId;
	}

	public void setNtfUsrId(String ntfUsrId) {
		this.ntfUsrId = ntfUsrId;
	}

	public String getNtfMessage() {
		return ntfMessage;
	}

	public void setNtfMessage(String ntfMessage) {
		this.ntfMessage = ntfMessage;
	}

	public LocalDate getNtfDate() {
		return ntfDate;
	}

	public void setNtfDate(LocalDate ntfDate) {
		this.ntfDate = ntfDate;
	}

	public Boolean getNtfRead() {
		return ntfRead;
	}

	public void setNtfRead(Boolean ntfRead) {
		this.ntfRead = ntfRead;
	}
}
