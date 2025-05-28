package com.cpt.model;

import java.util.ArrayList;
import java.util.List;

public class AttendedDrive {
	private int pldId;
	private String usrId;
	private String status;
	private int fizzledRound;
	private String pldName;
	private String cmpName;
	private List<HiringPhase> phases = new ArrayList<>();

	@Override
	public String toString() {
		return "AttendedDrive [pldId=" + pldId + ", usrId=" + usrId + ", status=" + status + ", fizzledRound="
				+ fizzledRound + ", pldName=" + pldName + ", cmpName=" + cmpName + ", phases=" + phases + "]";
	}

	public int getPldId() {
		return pldId;
	}

	public void setPldId(int pldId) {
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

	public int getFizzledRound() {
		return fizzledRound;
	}

	public void setFizzledRound(int fizzledRound) {
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

	public List<HiringPhase> getPhases() {
		return phases;
	}

	public void setPhases(List<HiringPhase> phases) {
		this.phases = phases;
	}

	public void addPhase(HiringPhase phase) {
		this.phases.add(phase);
	}
}