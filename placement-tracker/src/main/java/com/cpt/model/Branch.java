package com.cpt.model;

public class Branch {
    private Integer brnId;
    private Integer brnClgId;
    private String brnName;
    private String brnDesc;
    private College college;

    public Branch() {}

    public Integer getBrnId() { return brnId; }
    public void setBrnId(Integer brnId) { this.brnId = brnId; }
    public Integer getBrnClgId() { return brnClgId; }
    public void setBrnClgId(Integer brnClgId) { this.brnClgId = brnClgId; }
    public String getBrnName() { return brnName; }
    public void setBrnName(String brnName) { this.brnName = brnName; }
    public String getBrnDesc() { return brnDesc; }
    public void setBrnDesc(String brnDesc) { this.brnDesc = brnDesc; }

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	@Override
	public String toString() {
		return "Branch [brnId=" + brnId + ", brnClgId=" + brnClgId + ", brnName=" + brnName + ", brnDesc=" + brnDesc
				+ ", college=" + college + "]";
	}
	
}