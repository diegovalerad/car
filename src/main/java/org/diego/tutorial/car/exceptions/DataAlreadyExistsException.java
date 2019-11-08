package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class DataAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3962762829264287860L;

	public DataAlreadyExistsException(String message) {
		super(message);
	}
}
