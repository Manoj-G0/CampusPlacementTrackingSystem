package com.cpt.model;

public class AttendedDrive {
	private Integer pldId;
	private String usrId;
	private String status; // SEL, NOT
	private String fizzledRound;
	private String pldName;
	private String cmpName;
	private Integer hphId;
	private String hphName;
	private Integer hphSequence;

	// Getters and Setters
	public Integer getPldId() {
		return pldId;
	}

	public void setPldId(Integer pldId) {
		this.pldId = pldId;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFizzledRound() {
		return fizzledRound;
	}

	public void setFizzledRound(String fizzledRound) {
		this.fizzledRound = fizzledRound;
	}

	public String getPldName() {
		return pldName;
	}

	public void setPldName(String pldName) {
		this.pldName = pldName;
	}

	public String getCmpName() {
		return cmpName;
	}

	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}

	public Integer getHphId() {
		return hphId;
	}

	public void setHphId(Integer hphId) {
		this.hphId = hphId;
	}

	public String getHphName() {
		return hphName;
	}

	public void setHphName(String hphName) {
		this.hphName = hphName;
	}

	public Integer getHphSequence() {
		return hphSequence;
	}

	public void setHphSequence(Integer hphSequence) {
		this.hphSequence = hphSequence;
	}
}