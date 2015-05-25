package org.restfulpi.gpio;

import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.endpoint.GetPinResponse;
import org.restfulpi.endpoint.GetPinsResponse;

public class GPIORequestHandler {

	private final static Logger log = LogManager.getLogger();
	private static final PropertiesReader props = new PropertiesReader();
	ArrayList<NumberedPin> outputPins;
	ArrayList<NumberedPin> inputPins;
	
	private static final String INPUT_PINS_PROP_NAME = "inputPins";
	private static final String OUTPUT_PINS_PROP_NAME = "outputPins";
	
	public GPIORequestHandler() {
		outputPins = new ArrayList<NumberedPin>();
		inputPins = new ArrayList<NumberedPin>();
		try {
			outputPins.addAll(getNumberedPinsFromStrings(props.getProperty(OUTPUT_PINS_PROP_NAME).split(",")));
			inputPins.addAll(getNumberedPinsFromStrings(props.getProperty(INPUT_PINS_PROP_NAME).split(",")));
		} catch (Exception e) {
			log.error("Error Reading Properties File for inputPins or outputPins", e);
		}
	}

	private ArrayList<NumberedPin> getNumberedPinsFromStrings(String[] numberStrings) {
		ArrayList<NumberedPin> retList = new ArrayList<NumberedPin>();
		for(String currentPinString: numberStrings) {
			retList.add(getNumberedPinFromNumber(Integer.parseInt(currentPinString)));
		}
		return retList;
	}

	public GetPinsResponse getOutputPins() {
		return new GetPinsResponse(outputPins);
	}

	public GetPinsResponse getInputPins() {
		return new GetPinsResponse(inputPins);
	}

	public GetAllPinsResponse getPins() {
		return new GetAllPinsResponse(inputPins, outputPins);
	}

	public GetPinResponse provisionOutputPin(int pinNumber) {
		//TODO: implement
		return null;
	}
}
