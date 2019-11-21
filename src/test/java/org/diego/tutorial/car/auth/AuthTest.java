package org.diego.tutorial.car.auth;

import org.diego.tutorial.car.auth.jwt.JWT;
import org.diego.tutorial.car.exceptions.UnauthorizedException;
import org.diego.tutorial.car.model.Roles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * Set of unit tests for the {@link Auth} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JWT.class})
public class AuthTest {
	
	@Test
	public void testCheckToken() throws Exception {
		Claims claims = Mockito.mock(Claims.class);
		String token = "token";
		String method = "GET";
		String role = Roles.ADMIN.toString();
		
		PowerMockito.mockStatic(JWT.class);
		PowerMockito.when(JWT.class, "decodeJWT", token)
					.thenReturn(claims);
		Mockito.when(claims.get("role"))
				.thenReturn(role);
		
		Auth.checkToken(token, method);
		
		PowerMockito.verifyStatic(JWT.class);
		JWT.decodeJWT(token);
		Mockito.verify(claims).get("role");
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testCheckTokenNotValidAuthorizationHeader() throws Exception {
		Claims claims = Mockito.mock(Claims.class);
		String token = "token";
		String method = "GET";
		PowerMockito.mockStatic(JWT.class);
		PowerMockito.when(JWT.class, "decodeJWT", token)
					.thenThrow(JwtException.class);
		
		Auth.checkToken(token, method);
		
		PowerMockito.verifyStatic(JWT.class);
		JWT.decodeJWT(token);
		Mockito.verify(claims, Mockito.never()).get("role");
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testCheckTokenUnauthorized() throws Exception {
		Claims claims = Mockito.mock(Claims.class);
		String token = "token";
		String method = "POST";
		String role = Roles.USER.toString();
		
		PowerMockito.mockStatic(JWT.class);
		PowerMockito.when(JWT.class, "decodeJWT", token)
					.thenReturn(claims);
		Mockito.when(claims.get("role"))
				.thenReturn(role);
		
		Auth.checkToken(token, method);
		
		PowerMockito.verifyStatic(JWT.class);
		JWT.decodeJWT(token);
		Mockito.verify(claims).get("role");
	}

}
