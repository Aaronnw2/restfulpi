package org.restfulpi.gpio;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;
import static java.lang.String.format;
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
		NumberedPin inPin = getNumberedPinFromNumber(pinNumber);
		if(isProvisionedPin(pinNumber)) { return new HTTPResponse(false, format("Pin %d is already provisioned", pinNumber)); }
		try {
			OutputPin newPin = new OutputPin(inPin, inName,
					controller.provisionDigitalOutputPin(inPin.getPin(), inName,
							getStateFromString(inInitialState)));
			pins.add(newPin);
			return new GetPinResponse(newPin.getPinResponseInformation(), true, format("Pin %d provisioned as %s", pinNumber, inName));
		} catch(Exception e) {
			log.error("Error provisioning pin " + pinNumber, e);
			return new HTTPResponse(false, format("Error provisioning pin %d: %s", pinNumber, e.getMessage()));
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
		else return new HTTPResponse(false, format("Pin %d not provisioned", pinNumber));
	}

	public HTTPResponse setPinHigh(int pinNumber) {
		if(isProvisionedPin(pinNumber)) {
			GPIOPin pin = getPinByNumber(pinNumber);
			pin.processHigh();
			return new GetPinResponse(pin.getPinResponseInformation(), true, format("Pin %d set to high", pinNumber));
		}
		else return new HTTPResponse(false, format("Pin &d not provisioned", pinNumber));
	}

	public HTTPResponse setPinLow(int pinNumber) {
		if(isProvisionedPin(pinNumber)) {
			GPIOPin pin = getPinByNumber(pinNumber);
			pin.processLow();
			return new GetPinResponse(pin.getPinResponseInformation(), true, format("Pin %d set to low", pinNumber));
		}
		else return new HTTPResponse(false, format("Pin %d not provisioned", pinNumber));
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
		for(String currentPinString: numberStrings) {
			int pinNumber = Integer.parseInt(currentPinString.split(":")[0]);
			String pinName = currentPinString.split(":")[1];
			NumberedPin currentNumberedPin = getNumberedPinFromNumber(pinNumber);
			try {
				retList.add(new OutputPin(currentNumberedPin, pinName, controller.provisionDigitalOutputPin(currentNumberedPin.getPin(),
					pinName, LOW)));
			} catch (RuntimeException e) {
				log.error(format("Error provisioning pin %d as %s %s", pinNumber, pinName, e.getMessage()));
			}
		}
		return retList;
	}
	

	private PinState getStateFromString(String inInitialState) {
		if(inInitialState.toLowerCase().equals("high")) return HIGH;
		else if(inInitialState.toLowerCase().equals("low")) return LOW;
		else {
			log.error(format("Invalid pin state %s. Setting pin state to low", inInitialState));
			return LOW;
		}
	}
	

	private GPIOPin getPinByNumber(int pinNumber) {
		for(GPIOPin currentPin: pins) {
			if(currentPin.getNumberedPin().getGPIOPinNumber() == pinNumber) return currentPin;
		}
		log.error(format("Could not find pin for pin number %d. This pin should have been provisioned, but wasn't.", pinNumber));
		return null;
	}
}
