package org.restfulpi.gpio;

import org.restfulpi.response.PinInformation;

public interface GPIOPin {
	
	public NumberedPin getNumberedPin();
	public void setNumberedPin(NumberedPin numberedPin);
	public PinInformation getPinResponseInformation();
	public String getPinName();
	public void setPinName(String pinName);
}
