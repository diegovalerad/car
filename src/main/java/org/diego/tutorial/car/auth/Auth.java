package org.diego.tutorial.car.auth;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.auth.jwt.JWT;

import io.jsonwebtoken.JwtException;

/**
 * Class that connects the auth filter with the implemented auth filter.
 *
 */
public class Auth {
	private final static Logger LOGGER = Logger.getLogger(Auth.class);
	
	/**
	 * Checks if a token is valid
	 * @param token Token to check
	 * @return Boolean
	 */
	public static boolean checkToken(String token) {
		LOGGER.info("Checking the token '" + token + "'");
		try {
			JWT.decodeJWT(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}
}
