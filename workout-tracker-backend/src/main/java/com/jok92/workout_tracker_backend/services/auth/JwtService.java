package com.jok92.workout_tracker_backend.services.auth;

import com.jok92.workout_tracker_backend.models.auth.CustomUserDetail;
import com.jok92.workout_tracker_backend.repositories.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    
    @Autowired
    UserRepo userRepo;

    @Value("${jwt.secret}")
    private String signingKey;

    public String generateToken(String email) {
        UUID uuid = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found during token generation")).getId();
        Map<String, Object> claims = new HashMap<>();

        return generateToken(uuid);
    }

    public String generateToken(UUID uuid) {
        Map<String, Object> claims = new HashMap<>();

        long minutesInMilliseconds = Duration.ofDays(15).toMillis();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(uuid.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + minutesInMilliseconds))
                .and()
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        System.out.println(signingKey);
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signingKey));
    }

    public UUID extractID(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public boolean validateToken(String token, UserDetails user) {
        final UUID tokenId = extractID(token);
        CustomUserDetail customUserDetail = (CustomUserDetail) user;
        return (tokenId.equals(customUserDetail.getId()) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Claims claims;

        try {
            claims = Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            throw new BadCredentialsException("Expired token");
        }
        return claims;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
