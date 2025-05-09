package com.cpt.model;

import java.util.Date;

public class HrNotification {
    private int ntfId;
    private String ntfUsrId;
    private String ntfMessage;
    private Date ntfDate;
    private boolean ntfRead;

    // Getters and Setters
    public int getNtfId() { return ntfId; }
    public void setNtfId(int ntfId) { this.ntfId = ntfId; }
    public String getNtfUsrId() { return ntfUsrId; }
    public void setNtfUsrId(String ntfUsrId) { this.ntfUsrId = ntfUsrId; }
    public String getNtfMessage() { return ntfMessage; }
    public void setNtfMessage(String ntfMessage) { this.ntfMessage = ntfMessage; }
    public Date getNtfDate() { return ntfDate; }
    public void setNtfDate(Date ntfDate) { this.ntfDate = ntfDate; }
    public boolean isNtfRead() { return ntfRead; }
    public void setNtfRead(boolean ntfRead) { this.ntfRead = ntfRead; }
}