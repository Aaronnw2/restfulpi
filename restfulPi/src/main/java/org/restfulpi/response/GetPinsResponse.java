package org.restfulpi.response;

import java.util.ArrayList;

public class GetPinsResponse {

	private ArrayList<PinInformation> pins;
	private int totalPins;
	
	public GetPinsResponse() {}
	
	public GetPinsResponse(ArrayList<PinInformation> inPins) {
		pins = inPins;
		totalPins = pins.size();
	}
	
	public ArrayList<PinInformation> getOutlets() {
		return pins;
	}
	public void setOutlets(ArrayList<PinInformation> inPins) {
		pins = inPins;
	}
	public int getTotalPins() {
		return totalPins;
	}
	public void setTotalPins(int inTotalPins) {
		totalPins = inTotalPins;
	}
}
