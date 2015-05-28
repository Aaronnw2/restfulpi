package org.restfulpi.response;

import org.restfulpi.gpio.NumberedPin;

public class GetPinResponse {

	private NumberedPin pin;
	
	public GetPinResponse() {}

	public NumberedPin getPin() {
		return pin;
	}

	public void setPin(NumberedPin pin) {
		this.pin = pin;
	}
	
}