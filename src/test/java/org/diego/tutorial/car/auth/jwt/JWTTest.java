package org.diego.tutorial.car.auth.jwt;

import static org.junit.Assert.*;

import java.util.UUID;

import org.diego.tutorial.car.auth.jwt.JWT;
import org.junit.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JWTTest {

	@Test
	public void testCreateAndDecode() {
		String jwtId = UUID.randomUUID().toString();
		String jwtIssuer = "JWTTest";
        String jwtSubject = "Subject";
        int jwtTimeToLive = 800000;
        
        String jwt = JWT.createJWT(jwtId, jwtIssuer, jwtSubject, jwtTimeToLive);
        
        Claims claims = JWT.decodeJWT(jwt);
        
        assertEquals(jwtId, claims.getId());
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtSubject, claims.getSubject());
	}

	@Test(expected = MalformedJwtException.class)
    public void decodeNonValidJWT() {

        String notAJwt = "not a jwt";

        JWT.decodeJWT(notAJwt);
    }
	
	@Test(expected = SignatureException.class)
	public void decodeModifiedJWT() {
		String jwtId = UUID.randomUUID().toString();
		String jwtIssuer = "JWTTest";
        String jwtSubject = "Subject";
        int jwtTimeToLive = 8000000;
        
        String jwt = JWT.createJWT(jwtId, jwtIssuer, jwtSubject, jwtTimeToLive);
        
        StringBuilder tamperedJwt = new StringBuilder(jwt);
        tamperedJwt.setCharAt(22, 'D');
        String jwtModified = tamperedJwt.toString();

        assertNotEquals(jwt, tamperedJwt);
        
        JWT.decodeJWT(jwtModified);
	}
	
	
}
