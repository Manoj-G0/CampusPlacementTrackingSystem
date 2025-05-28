package com.cpt.model;

public class PhaseEvaluation {
    private Integer pevId;
    private Integer pevAppId;
    private Integer pevHphId;
    private Double pevScore;

    public PhaseEvaluation() {}

    public Integer getPevId() { return pevId; }
    public void setPevId(Integer pevId) { this.pevId = pevId; }
    public Integer getPevAppId() { return pevAppId; }
    public void setPevAppId(Integer pevAppId) { this.pevAppId = pevAppId; }
    public Integer getPevHphId() { return pevHphId; }
    public void setPevHphId(Integer pevHphId) { this.pevHphId = pevHphId; }
    public Double getPevScore() { return pevScore; }
    public void setPevScore(Double pevScore) { this.pevScore = pevScore; }
}
