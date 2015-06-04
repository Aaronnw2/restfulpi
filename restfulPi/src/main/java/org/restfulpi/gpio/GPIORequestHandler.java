package org.restfulpi.gpio;

import static org.restfulpi.PropertiesReader.OUTPUT_PINS_PROPERTY_NAME;
import static org.restfulpi.gpio.NumberedPin.getNumberedPinFromNumber;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.PropertiesReader;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;
import org.restfulpi.response.PinInformation;
import org.restfulpi.response.Response;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;

public class GPIORequestHandler {

	private final static Logger log = LogManager.getLogger();
	private static final PropertiesReader props = PropertiesReader.getInstance();
	
	//private static final GpioController controller = GpioFactory.getInstance();
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
			retList.add(new OutputPin(currentNumberedPin, "Dummy_name", null));
		}
		return retList;
	}

	public GetPinResponse provisionPin(int pinNumber, String inName, String inInitialState) {
		NumberedPin inPin = getNumberedPinFromNumber(pinNumber);
		try {
			OutputPin newPin= new OutputPin(inPin, inName, null);
			return new GetPinResponse(newPin.getPinResponseInformation(), true, "Pin " + pinNumber + " provisioned as " + inName);
		} catch(Exception e) {
			log.error("Error provisioning pin " + pinNumber, e);
			return new GetPinResponse(new PinInformation(pinNumber, inName, null), false, "Error provisioning pin " + pinNumber + ":" + e.getMessage());
		}
	}

	public GetPinsResponse getPins() {
		return new GetPinsResponse(buildPinInformationList(), true, "Request Completed");
	}

	private ArrayList<PinInformation> buildPinInformationList() {
		ArrayList<PinInformation> retList = new ArrayList<PinInformation>();
		for(GPIOPin currentPin: pins) {
			retList.add(currentPin.getPinResponseInformation());
		}
		return retList;
	}

	public GetPinResponse getPin(int pinNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public GetPinResponse setPinHigh(int pinNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public GetPinResponse setPinLow(int pinNumber) {
		// TODO Auto-generated method stub
		return null;
	}
}
