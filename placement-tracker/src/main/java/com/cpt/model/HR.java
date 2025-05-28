package com.cpt.model;

public class HR {
    private String hrId;
    private String hrName;
    private Integer cmpId;
	private Integer clgId;
    private String hrEmail;
    

    public HR() {}

    public String getHrId() { return hrId; }
    public void setHrId(String hrId) { this.hrId = hrId; }
    public String getHrName() { return hrName; }
    public void setHrName(String hrName) { this.hrName = hrName; }
    public Integer getCmpId() { return cmpId; }
    public void setCmpId(Integer cmpId) { this.cmpId = cmpId; }
    public String getHrEmail() { return hrEmail; }
    public void setHrEmail(String hrEmail) { this.hrEmail = hrEmail; }

	public Integer getClgId() {
		return clgId;
	}

	public void setClgId(Integer clgId) {
		this.clgId = clgId;
	}
}
