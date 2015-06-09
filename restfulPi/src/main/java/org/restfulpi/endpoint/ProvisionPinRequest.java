package org.restfulpi.endpoint;


public class ProvisionPinRequest {
	
	private String name;
	private String initialState;
	
	public ProvisionPinRequest() {
		name = "";
		initialState = "low";
	}
	
	public ProvisionPinRequest(String inName, String inInitialState) {
		name = inName;
		initialState = inInitialState;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitialState() {
		return initialState;
	}
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}
	
}
