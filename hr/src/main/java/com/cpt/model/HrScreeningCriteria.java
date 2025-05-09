package com.cpt.model;

import java.math.BigDecimal;

public class HrScreeningCriteria {
    private int scrId;
    private int scrPldId;
    private BigDecimal scrMinGpa;
    private int scrMinBacklogs;
    private int scrBrnId;
    private String scrGender;

    // Getters and Setters
    public int getScrId() { return scrId; }
    public void setScrId(int scrId) { this.scrId = scrId; }
    public int getScrPldId() { return scrPldId; }
    public void setScrPldId(int scrPldId) { this.scrPldId = scrPldId; }
    public BigDecimal getScrMinGpa() { return scrMinGpa; }
    public void setScrMinGpa(BigDecimal scrMinGpa) { this.scrMinGpa = scrMinGpa; }
    public int getScrMinBacklogs() { return scrMinBacklogs; }
    public void setScrMinBacklogs(int scrMinBacklogs) { this.scrMinBacklogs = scrMinBacklogs; }
    public int getScrBrnId() { return scrBrnId; }
    public void setScrBrnId(int scrBrnId) { this.scrBrnId = scrBrnId; }
    public String getScrGender() { return scrGender; }
    public void setScrGender(String scrGender) { this.scrGender = scrGender; }
}