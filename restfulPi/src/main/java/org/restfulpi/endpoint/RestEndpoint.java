package org.restfulpi.endpoint;

import static org.restfulpi.gpio.GPIORequestHandler.getInstance;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.gpio.GPIORequestHandler;
import org.restfulpi.gpio.ProvisionPinRequest;
import org.restfulpi.response.GetPinResponse;
import org.restfulpi.response.GetPinsResponse;
import org.restfulpi.response.Response;

@Path("/")
public class RestEndpoint {

	private static GPIORequestHandler controller = getInstance();
	private static final Logger log = LogManager.getLogger();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinsResponse getProvisionedOutputPins(@Context HttpServletRequest incomingRequest) {
		log.info("Get pins request from " + incomingRequest.getRemoteHost());
		return controller.getPins();
	}
	
	@GET
	@Path("/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	public GetPinResponse getPin(@Context HttpServletRequest incomingRequest, @PathParam("pin") int pinNumber) {
		log.info("Get Output pin request for " + pinNumber + " from " + incomingRequest.getRemoteHost());
		return controller.getPin(pinNumber);
	}
	
	@POST
	@Path("/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GetPinResponse provisionNewPin(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber, ProvisionPinRequest provisionRequest) {
		log.info("Provision pin request for " + pinNumber + " as \"stand in\"" + " from " + incomingRequest.getRemoteHost());
		return controller.provisionPin(pinNumber, provisionRequest.getName(), provisionRequest.getInitialState());
	}
	
	@PUT
	@Path("/{pin}/high")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPinHigh(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber) {
		log.info("Provision pin request for " + pinNumber + " from " + incomingRequest.getRemoteHost());
		return controller.setPinHigh(pinNumber);
	}
	
	@PUT
	@Path("/{pin}/low")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPinLow(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber) {
		log.info("Provision pin request for " + pinNumber + " from " + incomingRequest.getRemoteHost());
		return controller.setPinLow(pinNumber);
	}
}
