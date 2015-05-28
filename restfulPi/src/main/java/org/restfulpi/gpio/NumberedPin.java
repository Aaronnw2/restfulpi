package org.restfulpi.gpio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public enum NumberedPin {
	
	PIN0 (RaspiPin.GPIO_00, 0),
	PIN1 (RaspiPin.GPIO_01, 1),
	PIN2 (RaspiPin.GPIO_02, 2),
	PIN3 (RaspiPin.GPIO_03, 3),
	PIN4 (RaspiPin.GPIO_04, 4),
	PIN5 (RaspiPin.GPIO_05, 5),
	PIN6 (RaspiPin.GPIO_06, 6),
	PIN7 (RaspiPin.GPIO_07, 7),
	PIN8 (RaspiPin.GPIO_08, 8),
	PIN9 (RaspiPin.GPIO_09, 9),
	PIN10 (RaspiPin.GPIO_10, 10),
	PIN11 (RaspiPin.GPIO_11, 11),
	PIN12 (RaspiPin.GPIO_12, 12),
	PIN13 (RaspiPin.GPIO_13, 13),
	PIN14 (RaspiPin.GPIO_14, 14),
	PIN15 (RaspiPin.GPIO_15, 15),
	PIN16 (RaspiPin.GPIO_16, 16),
	PIN17 (RaspiPin.GPIO_17, 17),
	PIN18 (RaspiPin.GPIO_18, 18),
	PIN19 (RaspiPin.GPIO_19, 19),
	PIN20 (RaspiPin.GPIO_20, 20),
	PIN21 (RaspiPin.GPIO_21, 21),
	PIN22 (RaspiPin.GPIO_22, 22),
	PIN23 (RaspiPin.GPIO_23, 23),
	PIN24 (RaspiPin.GPIO_24, 24),
	PIN25 (RaspiPin.GPIO_25, 25),
	PIN26 (RaspiPin.GPIO_26, 26),
	PIN27 (RaspiPin.GPIO_27, 27),
	PIN28 (RaspiPin.GPIO_28, 28),
	PIN29 (RaspiPin.GPIO_29, 29),
	NO_SUCH_PIN (null, -1);
	
	private final int gpioPinNumber;
	private final Pin pin;
	
	public static final int MAX_PIN = 29;
	
	NumberedPin(Pin inPin, int inNumber) {
		this.pin = inPin;
		this.gpioPinNumber = inNumber;
	}
	
	public Pin getPin(){
		return this.pin;
	}
	
	public int getGPIOPinNumber(){
		return this.gpioPinNumber;
	}
	
	public NumberedPin getNumberedPinFromRaspiPin(RaspiPin inPin) {
		for(NumberedPin pin: NumberedPin.values()) {
			if(pin.getPin().equals(inPin)) return pin;
		}
		return NO_SUCH_PIN;
	}
	
	public static NumberedPin getNumberedPinFromNumber(int inNumber) {
		for(NumberedPin pin: NumberedPin.values()) {
			if(inNumber == pin.getGPIOPinNumber()) return pin;
		}
		return NO_SUCH_PIN;
	}
	
	@Override
	public String toString() {
		return pin.toString() + " " + gpioPinNumber;
	}
}
