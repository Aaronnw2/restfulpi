package org.restfulpi.gpio;

import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.endpoint.GetPinsResponse;

public class GPIORequestHandler {

	private final static Logger log = LogManager.getLogger();
	private static final PropertiesReader props = new PropertiesReader();
	ArrayList<NumberedPin> outputPins;
	ArrayList<NumberedPin> inputPins;
	
	public GPIORequestHandler() {
		try {
			outputPins.addAll(getNumberedPinsFromStrings(props.getProperty("outputPins").split(",")));
			inputPins.addAll(getNumberedPinsFromStrings(props.getProperty("inputPins").split(",")));
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
	
	
}
