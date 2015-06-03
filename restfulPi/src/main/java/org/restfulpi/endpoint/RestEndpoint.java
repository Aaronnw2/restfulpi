package org.restfulpi.endpoint;

import static org.restfulpi.gpio.GPIORequestHandler.getInstance;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.gpio.GPIORequestHandler;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;

@Path("/api")
public class RestEndpoint {

	private static GPIORequestHandler controller = getInstance();
	private static final Logger log = LogManager.getLogger();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinsResponse getProvisionedPins(@Context HttpServletRequest incomingRequest) {
		log.info("API request from " + incomingRequest.getRemoteHost());
		return controller.getPins();
	}
	
	@GET
	@Path("outputpins")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinsResponse getProvisionedOutputPins(@Context HttpServletRequest incomingRequest) {
		log.info("Get Output pins request from " + incomingRequest.getRemoteHost());
		return controller.getPins();
	}
	
	@GET
	@Path("outputpins/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinResponse getInputPin(@Context HttpServletRequest incomingRequest, @PathParam("pin") int pinNumber) {
		log.info("Get Output pin request for " + pinNumber + " from " + incomingRequest.getRemoteHost());
		return controller.getPin(pinNumber);
	}
	
	//TODO: post will include name and type (input/output)
	@POST
	@Path("outputpins/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinResponse provisionNewOutputPin(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber) {
		log.info("Provision pin request for " + pinNumber + " from " + incomingRequest.getRemoteHost());
		return controller.provisionOutputPin(pinNumber);
	}
}
