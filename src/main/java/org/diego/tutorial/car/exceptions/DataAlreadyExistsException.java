package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception that can be thrown when the client it is trying to add data that
 * already exists.
 *
 */
@ApplicationException
public class DataAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3962762829264287860L;

	/**
	 * Constructs a new DataAlreadyExists Exception with the specified detail message. 
	 * @param message Message that describes the exception
	 */
	public DataAlreadyExistsException(String message) {
		super(message);
	}
}
