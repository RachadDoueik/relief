package com.app.relief.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey; // Use javax.crypto.SecretKey for clarity
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // --- Public Methods ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    // --- Private Helper Methods ---

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(extraClaims) // Use .claims() instead of .setClaims()
                .subject(userDetails.getUsername()) // Use .subject() instead of .setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // Use .issuedAt() instead of .setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Use .expiration() instead of .setExpiration()
                .signWith(getSignInKey()) // Use the simplified signWith(SecretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        // Jwts.parser() now acts as the builder factory
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Use verifyWith() for clarity when checking tokens
                .build()
                .parseSignedClaims(token) // Parses a signed JWT and returns the claims JWS
                .getPayload(); // Renamed from getBody() to align with JWT spec
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
