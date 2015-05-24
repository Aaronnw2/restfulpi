package org.restfulpi.endpoint;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.gpio.GPIORequestHandler;

public class RestEndpoint {

	private static GPIORequestHandler controller = null;
	private static final Logger log = LogManager.getLogger();
	
	public RestEndpoint(Properties props) {
		controller = new GPIORequestHandler(props);
	}
	
	
}
