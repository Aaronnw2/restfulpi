package org.restfulpi.response;

public class GetPinResponse extends Response{

	private PinInformation pin;
	
	public GetPinResponse(PinInformation inPinInfo, Boolean inSuccess, String inMessage) {
		super(inSuccess, inMessage);
		pin = inPinInfo;
	}

	public PinInformation getPin() {
		return pin;
	}

	public void setPin(PinInformation pin) {
		this.pin = pin;
	}
	
}