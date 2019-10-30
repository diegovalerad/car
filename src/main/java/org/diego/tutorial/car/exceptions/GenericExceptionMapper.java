package org.diego.tutorial.car.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.diego.tutorial.car.model.ErrorMessage;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		String errorMessage = exception.getMessage();
		int errorCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		String documentation = "everis";
		ErrorMessage error = new ErrorMessage(errorMessage, errorCode, documentation);
		
		System.out.println("errorMessage: " + errorMessage);
		System.out.println("\n\n\n\n\n\n");
		
		return Response.status(errorCode)
					.entity(error)
					.build();
	}
	
}
