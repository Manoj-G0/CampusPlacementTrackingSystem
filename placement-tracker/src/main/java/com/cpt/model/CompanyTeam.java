package com.cpt.model;

public class CompanyTeam {
    private Integer ctmId;
    private Integer ctmCmpId;
    private String ctmName;
    private String ctmRole;
    private String ctmContact;

    public CompanyTeam() {}

    public Integer getCtmId() { return ctmId; }
    public void setCtmId(Integer ctmId) { this.ctmId = ctmId; }
    public Integer getCtmCmpId() { return ctmCmpId; }
    public void setCtmCmpId(Integer ctmCmpId) { this.ctmCmpId = ctmCmpId; }
    public String getCtmName() { return ctmName; }
    public void setCtmName(String ctmName) { this.ctmName = ctmName; }
    public String getCtmRole() { return ctmRole; }
    public void setCtmRole(String ctmRole) { this.ctmRole = ctmRole; }

	public String getCtmContact() {
		return ctmContact;
	}

	public void setCtmContact(String ctmContact) {
		this.ctmContact = ctmContact;
	}
}
