package org.diego.tutorial.car.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Class that provides a filter that modifies the header of every server response,
 * adding information about Everis.
 *
 */
@Provider
public class PoweredByResponseFilter implements ContainerResponseFilter {

	/**
	 * Filter that modifies the header of every server response, 
	 * adding information about Everis.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		responseContext.getHeaders().add("X-POWERED-BY", "everis");
	}
}
