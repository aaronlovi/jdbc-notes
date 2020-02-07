package com.seven.jdbc.beans;

import java.io.Serializable;

public class State implements Serializable {

	private static final long serialVersionUID = 6533171674753004583L;

	private String stateId;
	private String stateName;

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	///////////////////////////////////////////////////////////////////////////
	// PUBLIC INTERFACE
	///////////////////////////////////////////////////////////////////////////

	public void display() {
		System.out.println("State(id: " + getStateId() + ", name: " + getStateName() + ")");
	}
}
