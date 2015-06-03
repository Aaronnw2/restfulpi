package org.restfulpi.gpio;

import static org.restfulpi.PropertiesReader.OUTPUT_PINS_PROPERTY_NAME;
import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class GPIORequestHandler {

	private final static Logger log = LogManager.getLogger();
	private static final PropertiesReader props = PropertiesReader.getInstance();
	
	private static final GpioController controller = GpioFactory.getInstance();
	
	ArrayList<GPIOPin> pins;

	private static GPIORequestHandler HANDLER;
	
	public static GPIORequestHandler getInstance() {
		if(HANDLER == null) HANDLER = new GPIORequestHandler();
		return HANDLER;
	}
	
	protected GPIORequestHandler() {
		pins = new ArrayList<GPIOPin>();
		try {
			pins.addAll(getGPIOOutputPinFromStrings(
					props.getProperty(OUTPUT_PINS_PROPERTY_NAME).split(",")));
		} catch (Exception e) {
			log.error("Error loading initial configuration", e);
		}
	}

	private ArrayList<GPIOPin> getGPIOOutputPinFromStrings(String[] numberStrings) {
		ArrayList<GPIOPin> retList = new ArrayList<GPIOPin>();
		if(numberStrings[0].equals("")) return new ArrayList<GPIOPin>();
		//TODO: use <PinNumber>:<PinName>, for properties file
		for(String currentPinString: numberStrings) {
			NumberedPin currentNumberedPin = getNumberedPinFromNumber(Integer.parseInt(currentPinString));
			retList.add(new OutputPin(currentNumberedPin, "Dummy_name", controller.provisionDigitalOutputPin(currentNumberedPin.getPin())));
		}
		return retList;
	}

	public GetPinResponse provisionOutputPin(int pinNumber) {
		NumberedPin inPin = getNumberedPinFromNumber(pinNumber);
		controller.provisionDigitalInputPin(inPin.getPin(), "test");
		return null;
	}

	public GetPinsResponse getPins() {
		// TODO Auto-generated method stub
		return new GetPinsResponse();
	}

	public GetPinResponse getPin(int pinNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
