package org.example.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24; // 24 часа
//    знаю что нельзя оставлять такое в коде, но это предварительный варик

    private static final String SECRET_KEY_RAW = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJuYW1lIjoidGVzdHVzZXIiLCJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUxOTkwNjMyLCJleHAiOjE3NTIwNzcwMzJ9.WAeeOuKPpQGG8gi1sgAtUKuhNh2cBiGpvqPBRhT4h1I";

    private Key secretKey;

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Base64.getEncoder().encode(SECRET_KEY_RAW.getBytes());
        secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
