package com.app.catapi.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private static final long TOKEN_REFRESH_TIME = 1000 * 60 * 60 * 24 * 2;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
        );
        return generateToken(claims, userDetails.getUsername());
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignIngKey())
                .compact();

    }

    private SecretKey getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignIngKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String getUsername(String token) {
        Claims allClaims = getAllClaims(token);
        return allClaims.getSubject();
    }

    private Date getExpirationDate(String token) {
        Claims allClaims = getAllClaims(token);
        return allClaims.getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean canBeTokenRenewed(String token){
        return getExpirationDate(token).before(new Date(System.currentTimeMillis() + TOKEN_REFRESH_TIME));
    }

    public String renewToken(String token, UserDetails userDetails) {
        if(!canBeTokenRenewed(token)){
            throw new RuntimeException("Invalid token");
        }
        return generateToken(userDetails);
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        String username = getUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
