package org.restfulpi.response;

import java.util.ArrayList;

import org.restfulpi.gpio.NumberedPin;

public class GetAllPinsResponse {

	private ArrayList<PinInformation> inputPins;
	private ArrayList<PinInformation> outputPins;
	private int totalInputPins;
	private int totalOutputPins;
	private int totalPins;
	
	public GetAllPinsResponse() {}
	
	public GetAllPinsResponse(ArrayList<PinInformation> inputList, ArrayList<PinInformation> outputList) {
		inputPins = inputList;
		outputPins = outputList;
		totalInputPins = inputList.size();
		totalOutputPins = outputList.size();
		totalPins = totalInputPins + totalOutputPins;
	}
	
	public ArrayList<PinInformation> getInputPins() {
		return inputPins;
	}
	public void setInputPins(ArrayList<PinInformation> inputPins) {
		this.inputPins = inputPins;
	}
	public ArrayList<PinInformation> getOutputPins() {
		return outputPins;
	}
	public void setOutputPins(ArrayList<PinInformation> outputPins) {
		this.outputPins = outputPins;
	}
	public int getTotalInputPins() {
		return totalInputPins;
	}
	public void setTotalInputPins(int totalInputPins) {
		this.totalInputPins = totalInputPins;
	}
	public int getTotalOutputPins() {
		return totalOutputPins;
	}
	public void setTotalOutputPins(int totalOutputPins) {
		this.totalOutputPins = totalOutputPins;
	}
	public int getTotalPins() {
		return totalPins;
	}
	public void setTotalPins(int totalPins) {
		this.totalPins = totalPins;
	}
}
