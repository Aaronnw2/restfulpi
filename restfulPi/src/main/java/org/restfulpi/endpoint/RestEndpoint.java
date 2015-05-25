package org.restfulpi.endpoint;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.gpio.GPIORequestHandler;
import org.restfulpi.gpio.GetAllPinsResponse;

@Path("/api")
public class RestEndpoint {

	private static final GPIORequestHandler controller = new GPIORequestHandler();
	private static final Logger log = LogManager.getLogger();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GetAllPinsResponse getProvisionedPins(@Context HttpServletRequest incomingRequest) {
		log.info("Get Outlets request from " + incomingRequest.getRemoteHost());
		return controller.getPins();
	}
	
	@GET
	@Path("outputpins")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinsResponse getProvisionedOutputPins(@Context HttpServletRequest incomingRequest) {
		log.info("Get Outlets request from " + incomingRequest.getRemoteHost());
		return controller.getOutputPins();
	}
	
	@GET
	@Path("inputpins")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinsResponse getProvisionedInputPins(@Context HttpServletRequest incomingRequest) {
		log.info("Get Outlets request from " + incomingRequest.getRemoteHost());
		return controller.getInputPins();
	}
}
