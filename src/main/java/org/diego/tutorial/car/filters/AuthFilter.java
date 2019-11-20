package org.diego.tutorial.car.filters;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.auth.Auth;
import org.diego.tutorial.car.exceptions.UnauthorizedException;

/**
 * Authentication filter for the application, filtering every request
 *
 */
@Provider
public class AuthFilter implements ContainerRequestFilter  {

	private final static Logger LOGGER = Logger.getLogger(AuthFilter.class);
	
	public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	public static final String AUTHORIZATION_HEADER_TYPE = "Bearer";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.info("Checking auth");
		String errorMessage = "The request must contain a header with the key '" + AUTHORIZATION_HEADER_KEY + "', and a value "
				+ "in the following format: '" + AUTHORIZATION_HEADER_TYPE + "' <token>";
		boolean error = true;
		String token = null;
		
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() == 1) {
			String authToken = authHeader.get(0);
			
			String[] authComponents = authToken.split(" ");
			
			LOGGER.info("authToken: '" + authToken + "'");
			
			if (authComponents.length == 2 && authComponents[0].equals(AUTHORIZATION_HEADER_TYPE)){
				error = false;
				token = authComponents[1];
			}
		}
		
		if (error) {
			String message = "The request did not have auth header"; 
			LOGGER.info(message);
			throw new UnauthorizedException(message + ", " + errorMessage);
		}else {
			String method = requestContext.getRequest().getMethod();
			Auth.checkToken(token, method);
			LOGGER.info("Auth checked");
		}
		
		
	}
	
}
