package com.cpt.model;

import java.math.BigDecimal;
import java.util.List;

public class HrHiringPhase {
    private int hphId;
    private int hphPldId;
    private String hphName;
    private int hphSequence;
    private BigDecimal cutoffScore;
    private List<HrStudent> students;

    public List<HrStudent> getStudents() {
		return students;
	}
	public void setStudents(List<HrStudent> students) {
		this.students = students;
	}
	// Getters and Setters
    public int getHphId() { return hphId; }
    public void setHphId(int hphId) { this.hphId = hphId; }
    public int getHphPldId() { return hphPldId; }
    public void setHphPldId(int hphPldId) { this.hphPldId = hphPldId; }
    public String getHphName() { return hphName; }
    public void setHphName(String hphName) { this.hphName = hphName; }
    public int getHphSequence() { return hphSequence; }
    public void setHphSequence(int hphSequence) { this.hphSequence = hphSequence; }
    public BigDecimal getCutoffScore() { return cutoffScore; }
    public void setCutoffScore(BigDecimal cutoffScore) { this.cutoffScore = cutoffScore; }
}