package org.restfulpi.endpoint;

import static java.lang.String.format;
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
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restfulpi.gpio.GPIORequestHandler;
import org.restfulpi.response.HTTPResponse;

@Path("/")
public class RestEndpoint {

	private static GPIORequestHandler controller = getInstance();
	private static final Logger log = LogManager.getLogger();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProvisionedOutputPins(@Context HttpServletRequest incomingRequest) {
		log.info(format("Get pins request from %s", incomingRequest.getRemoteHost()));
		return buildResponse(controller.getPins());
	}
	
	@GET
	@Path("/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPin(@Context HttpServletRequest incomingRequest, @PathParam("pin") int pinNumber) {
		log.info(format("Get Output pin request for %d from %s", pinNumber, incomingRequest.getRemoteHost()));
		return buildResponse(controller.getPin(pinNumber));
	}

	@POST
	@Path("/{pin}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response provisionNewPin(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber, ProvisionPinRequest provisionRequest) {
		log.info(format("Provision pin request for %d as \"%s\" from %s", pinNumber, provisionRequest.getName(),
				incomingRequest.getRemoteHost()));
		return buildResponse(controller.provisionPin(pinNumber, provisionRequest.getName(), provisionRequest.getInitialState()));
	}
	
	@PUT
	@Path("/{pin}/high")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPinHigh(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber) {
		log.info(format("Set pin high request for %d from %s", pinNumber, incomingRequest.getRemoteHost()));
		return buildResponse(controller.setPinHigh(pinNumber));
	}
	
	@PUT
	@Path("/{pin}/low")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPinLow(@Context HttpServletRequest incomingRequest,
			@PathParam("pin") int pinNumber) {
		log.info(format("Set pin low request for %d from %s",pinNumber, incomingRequest.getRemoteHost()));
		return buildResponse(controller.setPinLow(pinNumber));
	}

	private Response buildResponse(HTTPResponse gpioResponse) {
		if(gpioResponse.isSuccess()) return Response.ok(gpioResponse).build();
		else return Response.serverError().entity(gpioResponse).build();
	}
}
