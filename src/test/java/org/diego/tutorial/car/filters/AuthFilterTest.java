package org.diego.tutorial.car.filters;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;

import org.diego.tutorial.car.auth.Auth;
import org.diego.tutorial.car.exceptions.UnauthorizedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Set of tests for the {@link AuthFilter} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Auth.class})
public class AuthFilterTest {
	@InjectMocks
	private AuthFilter authFilter;
	@Mock
	private ContainerRequestContext requestContext;
	
	@Test (expected = UnauthorizedException.class)
	public void testFilterNotAuthHeader() throws Exception {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headers = Mockito.mock(MultivaluedMap.class);
		
		Mockito.when(requestContext.getHeaders())
				.thenReturn(headers);
		Mockito.when(headers.get(AuthFilter.AUTHORIZATION_HEADER_KEY))
				.thenReturn(null);
		
		PowerMockito.mockStatic(Auth.class);
	
		PowerMockito.verifyStatic(Auth.class, Mockito.never());
		Auth.checkToken(Mockito.anyString(), Mockito.anyString());
		
		authFilter.filter(requestContext);
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testFilterWrongAuthHeaderSize() throws Exception {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headers = Mockito.mock(MultivaluedMap.class);
		List<String> authHeader = new ArrayList<String>();
		
		Mockito.when(requestContext.getHeaders())
				.thenReturn(headers);
		Mockito.when(headers.get(AuthFilter.AUTHORIZATION_HEADER_KEY))
				.thenReturn(authHeader);
		
		PowerMockito.mockStatic(Auth.class);
	
		PowerMockito.verifyStatic(Auth.class, Mockito.never());
		Auth.checkToken(Mockito.anyString(), Mockito.anyString());
		
		authFilter.filter(requestContext);
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testFilterWrongAuthComponents() throws Exception {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headers = Mockito.mock(MultivaluedMap.class);
		List<String> authHeader = new ArrayList<String>();
		String authComponent = "component";
		authHeader.add(authComponent);
		
		Mockito.when(requestContext.getHeaders())
				.thenReturn(headers);
		Mockito.when(headers.get(AuthFilter.AUTHORIZATION_HEADER_KEY))
				.thenReturn(authHeader);
		
		PowerMockito.mockStatic(Auth.class);
	
		PowerMockito.verifyStatic(Auth.class, Mockito.never());
		Auth.checkToken(Mockito.anyString(), Mockito.anyString());
		
		authFilter.filter(requestContext);
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testFilterCheckUnauthorized() throws Exception {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headers = Mockito.mock(MultivaluedMap.class);
		List<String> authHeader = new ArrayList<String>();
		String authToken = "token";
		String authComponent = AuthFilter.AUTHORIZATION_HEADER_TYPE + " " + authToken;
		authHeader.add(authComponent);
		
		Request request = Mockito.mock(Request.class);
		String method = "method";
		
		Mockito.when(requestContext.getHeaders())
				.thenReturn(headers);
		Mockito.when(headers.get(AuthFilter.AUTHORIZATION_HEADER_KEY))
				.thenReturn(authHeader);
		Mockito.when(requestContext.getRequest())
				.thenReturn(request);
		Mockito.when(request.getMethod())
				.thenReturn(method);
		
		PowerMockito.mockStatic(Auth.class);
		PowerMockito.doThrow(new UnauthorizedException("unauthorized"))
				.when(Auth.class, "checkToken", authToken, method);

		authFilter.filter(requestContext);
		
		PowerMockito.verifyStatic(Auth.class);
		Auth.checkToken(authToken, method);
	}
	
	@Test 
	public void testFilterOK() throws Exception {
		@SuppressWarnings("unchecked")
		MultivaluedMap<String, String> headers = Mockito.mock(MultivaluedMap.class);
		List<String> authHeader = new ArrayList<String>();
		String authToken = "token";
		String authComponent = AuthFilter.AUTHORIZATION_HEADER_TYPE + " " + authToken;
		authHeader.add(authComponent);
		
		Request request = Mockito.mock(Request.class);
		String method = "method";
		
		Mockito.when(requestContext.getHeaders())
				.thenReturn(headers);
		Mockito.when(headers.get(AuthFilter.AUTHORIZATION_HEADER_KEY))
				.thenReturn(authHeader);
		Mockito.when(requestContext.getRequest())
				.thenReturn(request);
		Mockito.when(request.getMethod())
				.thenReturn(method);
		
		PowerMockito.mockStatic(Auth.class);
		PowerMockito.doNothing()
				.when(Auth.class, "checkToken", authToken, method);

		authFilter.filter(requestContext);
		
		PowerMockito.verifyStatic(Auth.class);
		Auth.checkToken(authToken, method);
	}
}
