package org.diego.tutorial.car.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.diego.tutorial.car.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException exception) {
		String errorMessage = exception.getMessage();
		int errorCode = Status.NOT_FOUND.getStatusCode();
		String documentation = "everis";
		ErrorMessage error = new ErrorMessage(errorMessage, errorCode, documentation);
		
		return Response.status(errorCode)
					.entity(error)
					.build();
	}

}
