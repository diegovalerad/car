package org.diego.tutorial.car.filters;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

/**
 * Class that provides a basic authentication security, through filtering 
 * all requests 
 *
 */
//@Provider
public class SecurityFilter implements ContainerRequestFilter {

	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	
	/**
	 * Method that filters every request and checks if the user is authenticated
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
		if (authHeader != null && authHeader.size() > 0) {
			String authToken = authHeader.get(0);
			authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
			String decodedString = Base64.getDecoder().decode(authToken.getBytes()).toString();
			
			StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			
			if (userIsAuthenticated(username, password)) {
				return;
			}
			// TODO authentication incompleted
		}
		
		
	}

	/**
	 * Method that checks if an user is authenticated
	 * @param username Username of the user
	 * @param password Password of the user
	 * @return Boolean indicating if user and passwords are correct.
	 */
	private boolean userIsAuthenticated(String username, String password) {
		if (username.equals("username") && password.equals("password"))
			return true;
		return false;
	}
}
