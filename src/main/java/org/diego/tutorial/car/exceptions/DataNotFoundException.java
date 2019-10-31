package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class DataNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7673212887742966017L;
	
	public DataNotFoundException(String message) {
		super(message);
	}
}
