package com.cpt.model;

import java.util.ArrayList;
import java.util.List;

public class Round {
	private String roundName;
	private int sequence;
	private List<Parameter> parameters = new ArrayList<>();

	public String getRoundName() {
		return roundName;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
}