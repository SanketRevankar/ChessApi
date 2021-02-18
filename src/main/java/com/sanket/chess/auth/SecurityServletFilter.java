package com.sanket.chess.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurityServletFilter extends HttpFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    public SecurityServletFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = extractTokenId(request);
        if (token != null && !token.equals("null")) {
            UsernamePasswordAuthenticationToken auth = jwtTokenService.decodeToken(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

    private String extractTokenId(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(AUTHORIZATION);
        if (authenticationHeader != null && authenticationHeader.startsWith(PREFIX)) {
            return authenticationHeader.replace(PREFIX, "");
        }
        return null;
    }

}

