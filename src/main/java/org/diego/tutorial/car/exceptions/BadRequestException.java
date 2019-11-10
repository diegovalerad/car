package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception that can be thrown when the client makes a 
 * wrong request
 *
 */
@ApplicationException
public class BadRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3313493543444573212L;

	/**
	 * Constructs a new BadRequest Exception with the specified detail message. 
	 * @param message Message that describes the exception
	 */
	public BadRequestException (String message) {
		super(message);
	}
}
