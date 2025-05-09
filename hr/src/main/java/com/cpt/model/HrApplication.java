package com.cpt.model;

import java.util.Date;

public class HrApplication {
    private int appId;
    private String appUsrId;
    private int appPldId;
    private int appCmpId;
   private Date appDate;
    private String appStatus;
    private HrStudent student;

    public HrStudent getStudent() {
		return student;
	}
	public void setStudent(HrStudent student) {
		this.student = student;
	}
	// Getters and Setters
    public int getAppId() { return appId; }
    public void setAppId(int appId) { this.appId = appId; }
    public String getAppUsrId() { return appUsrId; }
    public void setAppUsrId(String appUsrId) { this.appUsrId = appUsrId; }
    public int getAppPldId() { return appPldId; }
    public void setAppPldId(int appPldId) { this.appPldId = appPldId; }
    public int getAppCmpId() { return appCmpId; }
    public void setAppCmpId(int appCmpId) { this.appCmpId = appCmpId; }
    public Date getAppDate() { return appDate; }
    public void setAppDate(Date appDate) { this.appDate = appDate; }
    public String getAppStatus() { return appStatus; }
    public void setAppStatus(String appStatus) { this.appStatus = appStatus; }
}