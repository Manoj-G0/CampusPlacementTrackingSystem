package com.cpt.model;

import java.sql.Date;
import java.util.List;

public class Application {
    private Integer appId;
    private String appUsrId;
    private Integer appPldId;
    private Integer appCmpId;
    private Date appDate;
    private String appStatus;
    private List<PhaseStatus> phaseStatuses;

    public Application() {}

    public Integer getAppId() { return appId; }
    public void setAppId(Integer appId) { this.appId = appId; }
    public String getAppUsrId() { return appUsrId; }
    public void setAppUsrId(String appUsrId) { this.appUsrId = appUsrId; }
    public Integer getAppPldId() { return appPldId; }
    public void setAppPldId(Integer appPldId) { this.appPldId = appPldId; }
    public Date getAppDate() { return appDate; }
    public void setAppDate(Date appDate) { this.appDate = appDate; }
    public String getAppStatus() { return appStatus; }
    public void setAppStatus(String appStatus) { this.appStatus = appStatus; }
    public List<PhaseStatus> getPhaseStatuses() { return phaseStatuses; }
    public void setPhaseStatuses(List<PhaseStatus> phaseStatuses) { this.phaseStatuses = phaseStatuses; }

	public Integer getAppCmpId() {
		return appCmpId;
	}

	public void setAppCmpId(Integer appCmpId) {
		this.appCmpId = appCmpId;
	}
}
