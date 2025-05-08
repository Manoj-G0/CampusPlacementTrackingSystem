package com.cpt.model;

import java.time.LocalDate;

public class Application {
	private Integer appId;
	private String appUsrId;
	private Integer appPldId;
	private Integer appCmpId;
	private LocalDate appDate;
	private String appStatus; // APPL, SHRT, REJD, OFFR

	// Getters and Setters
	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppUsrId() {
		return appUsrId;
	}

	public void setAppUsrId(String appUsrId) {
		this.appUsrId = appUsrId;
	}

	public Integer getAppPldId() {
		return appPldId;
	}

	public void setAppPldId(Integer appPldId) {
		this.appPldId = appPldId;
	}

	public Integer getAppCmpId() {
		return appCmpId;
	}

	public void setAppCmpId(Integer appCmpId) {
		this.appCmpId = appCmpId;
	}

	public LocalDate getAppDate() {
		return appDate;
	}

	public void setAppDate(LocalDate appDate) {
		this.appDate = appDate;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
}