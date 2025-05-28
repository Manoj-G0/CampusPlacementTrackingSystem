package com.cpt.model;

import java.util.ArrayList;
import java.util.List;

public class SelectedDrive {
	private int pldId;
	private String usrId;
	private String pldName;
	private String cmpName;
	private List<HiringPhase> phases = new ArrayList<>();


	@Override
	public String toString() {
		return "SelectedDrive [pldId=" + pldId + ", usrId=" + usrId + ", pldName=" + pldName + ", cmpName=" + cmpName
				+ ", phases=" + phases + "]";
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
