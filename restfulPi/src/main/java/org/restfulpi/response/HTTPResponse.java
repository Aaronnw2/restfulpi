package org.restfulpi.response;

public class HTTPResponse {

	protected boolean success;
	protected String message;
	
	public HTTPResponse(Boolean inSuccess, String inMessage) {
		success = inSuccess;
		message = inMessage;
	}
	
	public HTTPResponse() {
		success = false;
		message= "";
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
