package com.kubuski.urlshortener.config;

import com.kubuski.urlshortener.entity.User;
import com.kubuski.urlshortener.service.CustomUserDetailsService;
import com.kubuski.urlshortener.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_TYPE = "Authorization";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_TYPE);

        if (!isTokenPresent(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractToken(authHeader);
        String email = jwtService.extractEmail(jwt);

        if (isInvalidToken(email)) {
            filterChain.doFilter(request, response);
            return;
        }

        User userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(jwt, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(request, userDetails);
        filterChain.doFilter(request, response);
    }

    private boolean isTokenPresent(String authHeader) {
        return authHeader != null && authHeader.startsWith(TOKEN_PREFIX);
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(TOKEN_PREFIX.length());
    }

    private boolean isInvalidToken(String email) {
        return email == null || SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
