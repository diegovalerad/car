package org.diego.tutorial.car.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.ErrorMessage;

/**
 * Exception mapper that maps a {@link BadRequestException} exception to a {@link Response}, 
 * with a BAD REQUEST status code.
 * 
 */
@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

	private final static Logger LOGGER = Logger.getLogger(BadRequestExceptionMapper.class);
	
	@Override
	public Response toResponse(BadRequestException exception) {
		String errorMessage = exception.getMessage();
		int errorCode = Status.BAD_REQUEST.getStatusCode();
		String documentation = "Contact to Everis if this error persists.";
		ErrorMessage error = new ErrorMessage(errorMessage, errorCode, documentation);
		
		LOGGER.warn("Something went wrong!", exception);
		LOGGER.info("A response with the error is being created by the BadRequestExceptionMapper");
		
		return Response.status(errorCode)
					.entity(error)
					.build();
	}

}
