package org.restfulpi.gpio;

import org.restfulpi.response.PinInformation;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;

public class InputPin implements GPIOPin{

	private NumberedPin numberedPin;
	private GpioPinDigitalInput provisionedPin;
	private String pinName;
	
	public InputPin(NumberedPin inNumberedPin, String inPinName, GpioPinDigitalInput inProvisionedPin) {
		numberedPin = inNumberedPin;
		pinName = inPinName;
		setProvisionedPin(inProvisionedPin);
	}
	
	
	public NumberedPin getNumberedPin() {
		return numberedPin;
	}

	public void setNumberedPin(NumberedPin inNumberedPin) {
		numberedPin = inNumberedPin;
	}

	public PinInformation getPinResponseInformation() {
		return new PinInformation(numberedPin.getGPIOPinNumber(), getPinName(), getPinState());
	}

	private PinState getPinState() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPinName() {
		return pinName;
	}

	public void setPinName(String inPinName) {
		pinName = inPinName;
	}


	public GpioPinDigitalInput getProvisionedPin() {
		return provisionedPin;
	}


	public void setProvisionedPin(GpioPinDigitalInput provisionedPin) {
		this.provisionedPin = provisionedPin;
	}
}
