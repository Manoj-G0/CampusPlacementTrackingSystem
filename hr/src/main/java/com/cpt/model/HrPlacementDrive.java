package com.cpt.model;

import java.math.BigDecimal;
import java.util.Date;

public class HrPlacementDrive {
    private int pldId;
    private int pldClgId;
    private int pldCmpId;
    private String pldName;
    private String pldRole;
    private BigDecimal pldPackage;
    private Date pldStartDate;
    private Date pldEndDate;
    private String pldStatus;
    private String cmpDesc; // For completed drives display
    private int applicationCount;

    public int getApplicationCount() {
		return applicationCount;
	}
	public void setApplicationCount(int applicationCount) {
		this.applicationCount = applicationCount;
	}
	// Getters and Setters
    public int getPldId() { return pldId; }
    public void setPldId(int pldId) { this.pldId = pldId; }
    public int getPldClgId() { return pldClgId; }
    public void setPldClgId(int pldClgId) { this.pldClgId = pldClgId; }
    public int getPldCmpId() { return pldCmpId; }
    public void setPldCmpId(int pldCmpId) { this.pldCmpId = pldCmpId; }
    public String getPldName() { return pldName; }
    public void setPldName(String pldName) { this.pldName = pldName; }
    public String getPldRole() { return pldRole; }
    public void setPldRole(String pldRole) { this.pldRole = pldRole; }
    public BigDecimal getPldPackage() { return pldPackage; }
    public void setPldPackage(BigDecimal pldPackage) { this.pldPackage = pldPackage; }
    public Date getPldStartDate() { return pldStartDate; }
    public void setPldStartDate(Date pldStartDate) { this.pldStartDate = pldStartDate; }
    public Date getPldEndDate() { return pldEndDate; }
    public void setPldEndDate(Date pldEndDate) { this.pldEndDate = pldEndDate; }
    public String getPldStatus() { return pldStatus; }
    public void setPldStatus(String pldStatus) { this.pldStatus = pldStatus; }
    public String getCmpDesc() { return cmpDesc; }
    public void setCmpDesc(String cmpDesc) { this.cmpDesc = cmpDesc; }
}