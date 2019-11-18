package org.diego.tutorial.car.auth.jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

/**
 * Class that implements the authentication for the backend using JWT (JSON Web Token)
 *
 */
public class JWT {
	private static String SECRET_KEY = "secret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_key";
	
	private final static Logger LOGGER = Logger.getLogger(JWT.class);
	
	public static String createJWT(String id, String issuer, String subject, long ttlInMillis) {
		LOGGER.info("Creating jwt");
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signingKey, signatureAlgorithm);
		
		if (ttlInMillis >= 0) {
            long expMillis = nowMillis + ttlInMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
		
		LOGGER.info("jwt created: " + builder.compact());
		
		//Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
	}
	
	public static Claims decodeJWT(String jwt) {
		LOGGER.info("Decoding jwt: '" + jwt + "'");
		
		Jws<Claims> jws = null;
		
		jws = Jwts.parser()
			    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))      
			    .parseClaimsJws(jwt);
	    
	    // we can safely trust the JWT
	    return jws.getBody();
	}
}
