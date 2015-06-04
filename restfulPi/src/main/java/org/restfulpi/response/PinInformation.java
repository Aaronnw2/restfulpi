package org.restfulpi.response;

import com.pi4j.io.gpio.PinState;

public class PinInformation {

	private int pinNumber;
	private String pinName;
	private PinState state;
	
	public PinInformation() {
		pinNumber = -1;
		pinName = "";
		state = PinState.LOW;
	}
	
	public PinInformation(int inPinNumber, String inPinName, PinState inPinState) {
		pinNumber = inPinNumber;
		pinName = inPinName;
		state = inPinState;
	}
	
	public int getPinNumber() {
		return pinNumber;
	}
	public void setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
	}
	public String getPinName() {
		return pinName;
	}
	public void setPinName(String pinName) {
		this.pinName = pinName;
	}
	public PinState getState() {
		return state;
	}
	public void setState(PinState state) {
		this.state = state;
	}
	
}
