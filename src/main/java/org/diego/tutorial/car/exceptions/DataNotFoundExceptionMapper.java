package org.diego.tutorial.car.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.ErrorMessage;

/**
 * Exception mapper that maps a {@link DataNotFoundException} exception to a {@link Response}, 
 * with a NOT FOUND status code.
 * 
 */
@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {
	
	private final static Logger LOGGER = Logger.getLogger(DataNotFoundExceptionMapper.class);

	@Override
	public Response toResponse(DataNotFoundException exception) {
		String errorMessage = exception.getMessage();
		int errorCode = Status.NOT_FOUND.getStatusCode();
		String documentation = "Contact to Everis if this error persists.";
		ErrorMessage error = new ErrorMessage(errorMessage, errorCode, documentation);
		
		LOGGER.warn("Something went wrong!", exception);
		LOGGER.info("A response with the error is being created by the DataNotFoundExceptionMapper");
		
		return Response.status(errorCode)
					.entity(error)
					.build();
	}

}
