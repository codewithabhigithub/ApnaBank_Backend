 package com.login.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
	private static final String secret="397A244326452948404D635166546A576E5A7234753778214125442A472D4A61";
	public String generateToken(String userName) {
		Map<String,Object> claims=new HashMap<>();
		return createToken(claims,userName);
	}
	public String createToken(Map<String,Object> claims,String userName) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(getKey(),SignatureAlgorithm.HS256).compact();
	}
	public Key getKey() {
		byte[] securitykey=Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(securitykey);
	}
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build()
				.parseClaimsJws(token).getBody();
	}
	public <T> T extractClaim(String token,Function<Claims,T> claimsResolver) {
		final Claims claim=extractAllClaims(token);
		return claimsResolver.apply(claim);
	}
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	public void validateToken(String token) {
		 Jwts.parserBuilder().setSigningKey(getKey()).build()
		.parseClaimsJws(token);
	}
	
	
}