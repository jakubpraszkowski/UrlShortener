package com.kubuski.urlshortener.service;

import com.kubuski.urlshortener.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final int EXPIRATION_MINUTES = 60 * 24;
    private static final String EMAIL_CLAIM = "email";

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get(EMAIL_CLAIM, String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, User userDetails) {
        String subjectUsername = extractSubject(token);
        String email = extractEmail(token);

        return email.equals(userDetails.getEmail())
                && subjectUsername.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);

        return Jwts.builder().claims(extraClaims).subject(userDetails.getUsername())
                .claim(EMAIL_CLAIM, userDetails.getEmail()).issuedAt(Date.from(now))
                .expiration(Date.from(expiration)).signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token)
                .getPayload();
    }

    boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
