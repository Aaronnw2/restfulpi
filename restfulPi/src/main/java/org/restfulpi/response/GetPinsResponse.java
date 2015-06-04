package org.restfulpi.response;

import java.util.ArrayList;

public class GetPinsResponse extends Response {

	private ArrayList<PinInformation> pins;
	private int totalPins;
	
	public GetPinsResponse() {}
	
	public GetPinsResponse(ArrayList<PinInformation> inPins, Boolean inSuccess, String inMessage) {
		super(inSuccess, inMessage);
		pins = inPins;
		totalPins = pins.size();
	}
	
	public ArrayList<PinInformation> getPins() {
		return pins;
	}
	public void setPins(ArrayList<PinInformation> inPins) {
		pins = inPins;
	}
	public int getTotalPins() {
		return totalPins;
	}
	public void setTotalPins(int inTotalPins) {
		totalPins = inTotalPins;
	}
}
