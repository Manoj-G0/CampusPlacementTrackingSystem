package com.cpt.model;

public class PhaseStatus {
    private Integer phaseId;
    private String status;

    public PhaseStatus(Integer phaseId, String status) {
        this.phaseId = phaseId;
        this.status = status;
    }

    public Integer getPhaseId() { return phaseId; }
    public void setPhaseId(Integer phaseId) { this.phaseId = phaseId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
