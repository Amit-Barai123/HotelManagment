package com.amit.hotelManagment.security.jwt;


import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
private final SecretKey key;

private final Long expiration;

public JwtService(
		@Value("${app.jwt.secret}") String secret,
		@Value("${app.secret.expiration}") Long expiration){
			this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			this.expiration = expiration;
		}
		
public String generateToken(UserDetails userDetails) {
	Date now  = new Date();
	
	return Jwts.builder()
			.subject(userDetails.getUsername())
			.issuedAt(now)
			.expiration(new Date(now.getTime() + expiration))
			.signWith(key)
			.compact();
}

public String extractUsername(String token) {
	return parseClaims(token).getSubject();
}

private Claims parseClaims(String token) {
	
	return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
			
}

public boolean isTokenValid(String token, UserDetails userDetails) {
	try {
		return extractUsername(token).equals(userDetails.getUsername()) &&
				!parseClaims(token).getExpiration().before(new Date());
	} catch (Exception e) {
		return false;
	}
}


}
