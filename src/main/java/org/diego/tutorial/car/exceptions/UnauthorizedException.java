package org.diego.tutorial.car.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception that can be thrown when the client makes an
 * unauthorized request
 *
 */
@ApplicationException
public class UnauthorizedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6355406104581227669L;
	
	public UnauthorizedException(String message) {
		super(message);
	}
	
}
