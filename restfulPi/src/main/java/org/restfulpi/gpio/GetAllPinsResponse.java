package org.restfulpi.gpio;

import java.util.ArrayList;

public class GetAllPinsResponse {

	private ArrayList<NumberedPin> inputPins;
	private ArrayList<NumberedPin> outputPins;
	private int totalInputPins;
	private int totalOutputPins;
	private int totalPins;
	
	public GetAllPinsResponse() {}
	
	public GetAllPinsResponse(ArrayList<NumberedPin> inputList, ArrayList<NumberedPin> outputList) {
		inputPins = inputList;
		outputPins = outputList;
		totalInputPins = inputList.size();
		totalOutputPins = outputList.size();
		totalPins = totalInputPins + totalOutputPins;
	}
	
	public ArrayList<NumberedPin> getInputPins() {
		return inputPins;
	}
	public void setInputPins(ArrayList<NumberedPin> inputPins) {
		this.inputPins = inputPins;
	}
	public ArrayList<NumberedPin> getOutputPins() {
		return outputPins;
	}
	public void setOutputPins(ArrayList<NumberedPin> outputPins) {
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
