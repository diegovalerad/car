package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception that can be thrown when the client it is trying to access to data that
 * does not exist.
 *
 */
@ApplicationException
public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7673212887742966017L;
	
	/**
	 * Constructs a new DataNotFoundException Exception with the specified detail message. 
	 * @param message Message that describes the exception
	 */
	public DataNotFoundException(String message) {
		super(message);
	}
}
