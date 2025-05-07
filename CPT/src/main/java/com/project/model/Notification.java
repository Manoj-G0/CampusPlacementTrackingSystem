package com.project.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Notification {
	private Short id;
	private Short userId;
	private String message;

	public Notification() {
		super();
	}

	public Notification(Short id, Short userId, String message, LocalDate date, Boolean read) {
		super();
		this.id = id;
		this.userId = userId;
		this.message = message;
		this.date = date;
		this.read = read;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Short getUserId() {
		return userId;
	}

	public void setUserId(Short userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	private LocalDate date;
	private Boolean read;
}