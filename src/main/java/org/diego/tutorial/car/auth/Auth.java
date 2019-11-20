package org.diego.tutorial.car.auth;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.auth.jwt.JWT;
import org.diego.tutorial.car.exceptions.UnauthorizedException;
import org.diego.tutorial.car.model.Roles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * Class that connects the auth filter with the implemented auth filter.
 *
 */
public class Auth {
	private final static Logger LOGGER = Logger.getLogger(Auth.class);
	
	/**
	 * Checks if a token is valid. If it is valid, then the method of the request is checked;
	 * only users with admin {@link Roles} can modify information. It throws: <p>
	 * <ul>
	 * <li> {@link UnauthorizedException} if the auth header is not valid or if the user does not have enough privileges. 
	 * </ul>
	 * @param token Token to check
	 * @param method Method of the request.
	 */
	public static void checkToken(String token, String method) {
		LOGGER.info("Checking the token '" + token + "', method: '" + method + "'");
		Claims claims = null;
		try {
			claims = JWT.decodeJWT(token);
		} catch (JwtException e) {
			String message = "The request did not have a valid authorization header"; 
			LOGGER.info(message);
			throw new UnauthorizedException(message);
		}
		
		String roleString = (String) claims.get("role"); 
		Roles role = Roles.valueOf(roleString);
		
		if (role.equals(Roles.USER)) {
			if (!method.equals("GET")) {
				String message = "The user does not have enough privileges"; 
				LOGGER.info(message);
				throw new UnauthorizedException(message);
			}
		}
	}
}
