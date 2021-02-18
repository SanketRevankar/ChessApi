package com.sanket.chess.auth;

import com.sanket.chess.mongodb.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenService implements Serializable {
    private static final long serialVersionUID = -1232142142109L;
    public static final long JWT_TOKEN_VALIDITY = 8440000;

    private final Key signingKey;

    public JwtTokenService(@Value("${jwt.secret}") String secret) {
        signingKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setId(user.getId())
                .setSubject(user.getFullName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public UsernamePasswordAuthenticationToken decodeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();

            UserDetails userDetails = new User(claims.getId(), claims.getSubject());
            return new UsernamePasswordAuthenticationToken(userDetails, null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + claims.getId())));
        } catch (ExpiredJwtException | SignatureException e) {
            return null;
        }
    }
}