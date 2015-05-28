package org.restfulpi.gpio;

import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.response.GetAllPinsResponse;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class GPIORequestHandler {

	private final static Logger log = LogManager.getLogger();
	private static final PropertiesReader props = new PropertiesReader();
	
	private static GpioController controller = GpioFactory.getInstance();
	
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
			log.error("Error loading initial configuration", e);
		}
	}
	
	protected GPIORequestHandler(GpioController mockController) {
		controller = mockController;  
	}

	private ArrayList<NumberedPin> getNumberedPinsFromStrings(String[] numberStrings) {
		ArrayList<NumberedPin> retList = new ArrayList<NumberedPin>();
		for(String currentPinString: numberStrings) {
			retList.add(getNumberedPinFromNumber(Integer.parseInt(currentPinString)));
		}
		return retList;
	}

	public GetPinResponse provisionOutputPin(int pinNumber) {
		NumberedPin inPin = getNumberedPinFromNumber(pinNumber);
		controller.provisionDigitalInputPin(inPin.getPin(), "test");
		return null;
	}

	public GetAllPinsResponse getPins() {
		return new GetAllPinsResponse(inputPins, outputPins);
	}

	public GetPinsResponse getOutputPins() {
		return new GetPinsResponse(outputPins);
	}

	public GetPinsResponse getInputPins() {
		return new GetPinsResponse(inputPins);
	}
}
