package org.restfulpi.gpio;

import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.PinInformation;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class OutputPin implements GPIOPin{

	private GpioPinDigitalOutput provisionedPin;
	private NumberedPin numberedPin;
	private String pinName;	
	
	public OutputPin(NumberedPin inNumberedPin, String inName, GpioPinDigitalOutput inProvisionedPin) {
		numberedPin = inNumberedPin;
		pinName = inName;
		
	}
	
	public NumberedPin getNumberedPin() {
		return numberedPin;
	}

	public void setNumberedPin(NumberedPin inNumberedPin) {
		numberedPin = inNumberedPin;
	}

	public PinInformation getPinResponseInformation() {
		return new PinInformation(numberedPin.getGPIOPinNumber(), pinName, provisionedPin.getState());
	}

	public String getPinName() {
		return pinName;
	}

	public void setPinName(String inPinName) {
		pinName = inPinName;
	}

	public GpioPinDigitalOutput getProvisionedPin() {
		return provisionedPin;
	}

	public void setProvisionedPin(GpioPinDigitalOutput provisionedPin) {
		this.provisionedPin = provisionedPin;
	}

	public GetPinResponse processHigh() {
		// TODO Auto-generated method stub
		return null;
	}

	public GetPinResponse processLow() {
		// TODO Auto-generated method stub
		return null;
	}

	public GetPinResponse processStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
