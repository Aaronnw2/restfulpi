package org.restfulpi.gpio;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static org.restfulpi.PropertiesReader.OUTPUT_PINS_PROPERTY_NAME;
import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;
import org.restfulpi.response.HTTPResponse;
import org.restfulpi.response.PinInformation;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;

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

	public HTTPResponse provisionPin(int pinNumber, String inName, String inInitialState) {
		log.info("In gpio controller");
		NumberedPin inPin = getNumberedPinFromNumber(pinNumber);
		log.info("Nubmered pin found");
		try {
			OutputPin newPin = new OutputPin(inPin, inName,
					controller.provisionDigitalOutputPin(inPin.getPin(), inName,
							getStateFromString(inInitialState)));
			//OutputPin newPin = new OutputPin(inPin, inName, null);
			log.info("Pin provisioned");//TODO: remove all debug comments
			pins.add(newPin);
			return new GetPinResponse(newPin.getPinResponseInformation(), true, "Pin " + pinNumber + " provisioned as " + inName);
		} catch(Exception e) {
			log.error("Error provisioning pin " + pinNumber, e);
			return new HTTPResponse(false, "Error provisioning pin " + pinNumber + ":" + e.getMessage());
		}
	}

	public HTTPResponse getPins() {
		return new GetPinsResponse(buildPinInformationList(), true, "Request Completed");
	}

	public HTTPResponse getPin(int pinNumber) {
		if(isProvisionedPin(pinNumber)) {
			GPIOPin pin = getPinByNumber(pinNumber);
			return new GetPinResponse(pin.getPinResponseInformation(), true, "Request Completed");
		}
		else return new HTTPResponse(false, "Pin not provisioned");
	}

	public HTTPResponse setPinHigh(int pinNumber) {
		if(isProvisionedPin(pinNumber)) {
			GPIOPin pin = getPinByNumber(pinNumber);
			pin.processHigh();
			return new GetPinResponse(pin.getPinResponseInformation(), true, "Request Completed");
		}
		else return new HTTPResponse(false, "Pin not provisioned");
	}

	public HTTPResponse setPinLow(int pinNumber) {
		if(isProvisionedPin(pinNumber)) {
			GPIOPin pin = getPinByNumber(pinNumber);
			pin.processLow();
			return new GetPinResponse(pin.getPinResponseInformation(), true, "Request Completed");
		}
		else return new HTTPResponse(false, "Pin not provisioned");
	}
	
	private boolean isProvisionedPin(int pinNumber) {
		for(GPIOPin currentPin: pins) {
			if(currentPin.getNumberedPin().getGPIOPinNumber() == pinNumber) return true;
		}
		return false;
	}
	
	private ArrayList<PinInformation> buildPinInformationList() {
		ArrayList<PinInformation> retList = new ArrayList<PinInformation>();
		for(GPIOPin currentPin: pins) {
			retList.add(currentPin.getPinResponseInformation());
		}
		return retList;
	}
	
	private ArrayList<GPIOPin> getGPIOOutputPinFromStrings(String[] numberStrings) {
		ArrayList<GPIOPin> retList = new ArrayList<GPIOPin>();
		if(numberStrings[0].equals("")) return new ArrayList<GPIOPin>();
		//TODO: use <PinNumber>:<PinName>, for properties file
		for(String currentPinString: numberStrings) {
			NumberedPin currentNumberedPin = getNumberedPinFromNumber(Integer.parseInt(currentPinString));
			retList.add(new OutputPin(currentNumberedPin, "Dummy_name", null));
		}
		return retList;
	}
	

	private PinState getStateFromString(String inInitialState) {
		if(inInitialState.toLowerCase().equals("high")) return HIGH;
		else if(inInitialState.toLowerCase().equals("low")) return LOW;
		else {
			log.error("Invalid pin state " + inInitialState + ". Setting pin state to low");
			return LOW;
		}
	}
	

	private GPIOPin getPinByNumber(int pinNumber) {
		for(GPIOPin currentPin: pins) {
			if(currentPin.getNumberedPin().getGPIOPinNumber() == pinNumber) return currentPin;
		}
		log.error("Could not find pin for pin number " + pinNumber + ". This pin should have been provisioned, but wasn't.");
		return null;
	}
}
