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
		provisionedPin = inProvisionedPin;
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
		if(provisionedPin.isHigh()) return new GetPinResponse(getPinResponseInformation(),
				true, "Pin is already high");
		else {
			provisionedPin.high();
			return new GetPinResponse(getPinResponseInformation(),
					true, "Pin set to high");
		}
	}

	public GetPinResponse processLow() {
		if(provisionedPin.isLow()) return new GetPinResponse(getPinResponseInformation(),
				true, "Pin is already low");
		else {
			provisionedPin.low();
			return new GetPinResponse(getPinResponseInformation(),
					true, "Pin set to low");
		}
	}

	public GetPinResponse processStatus() {
		return new GetPinResponse(getPinResponseInformation(), true, "Request Complete");
	}
}
