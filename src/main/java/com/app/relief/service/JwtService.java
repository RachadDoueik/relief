package com.app.relief.service;

import com.app.relief.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.access-expiration}")
    private long accessExpiration;

    // ================= TOKEN CREATION ===================

    public String generateAccessToken(User user) {
        return buildToken(user.getUsername(), accessExpiration);
    }

    private String buildToken(String username, long expirationMs) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    // ================= TOKEN EXTRACTION ==================

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    // ================= VALIDATION ========================

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }

    // ================= HELPERS ===========================

    public long getAccessTokenExpiration() {
        return accessExpiration;
    }

    private SecretKey getKey() {
        byte[] decoded = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(decoded);
    }
}
