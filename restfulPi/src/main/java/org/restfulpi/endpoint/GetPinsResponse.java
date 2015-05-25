package org.restfulpi.endpoint;

import java.util.ArrayList;

import org.restfulpi.gpio.NumberedPin;

public class GetPinsResponse {

	private ArrayList<NumberedPin> pins;
	private int totalPins;
	
	public GetPinsResponse() {}
	
	public GetPinsResponse(ArrayList<NumberedPin> inPins) {
		pins = inPins;
		totalPins = pins.size();
	}
	
	public ArrayList<NumberedPin> getOutlets() {
		return pins;
	}
	public void setOutlets(ArrayList<NumberedPin> inPins) {
		pins = inPins;
	}
	public int getTotalPins() {
		return totalPins;
	}
	public void setTotalPins(int inTotalPins) {
		totalPins = inTotalPins;
	}
}
