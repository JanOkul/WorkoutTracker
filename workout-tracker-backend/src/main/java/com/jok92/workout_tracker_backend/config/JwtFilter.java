package com.jok92.workout_tracker_backend.config;

import com.jok92.workout_tracker_backend.services.auth.CustomUserDetailService;
import com.jok92.workout_tracker_backend.services.auth.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;


enum JwtError {
    JWT_EXPIRED,
    SIGNATURE_INVALID,
    MALFORMED,
    UNSUPPORTED
}


@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getCookieValue(request, "accessToken");
        UUID id;
        logger.debug("Access JWT token value: {}", accessToken);

        if (accessToken == null) {
            logger.debug("Access token null");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            id = jwtService.extractID(accessToken);
            logger.debug("User ID for access token: {}", id);
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token expired {}", e.getMessage());
            handleJwtError(response, JwtError.JWT_EXPIRED);
            return;
        }


        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(CustomUserDetailService.class).loadUserById(id);

            if (jwtService.validateToken(accessToken, userDetails)) {
                logger.debug("Token valid");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.debug("Token invalid");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            logger.debug("No cookies");
            return null;
        }

        logger.debug("Getting cookies array: {}", Arrays.toString(request.getCookies()));
        return Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public void handleJwtError(HttpServletResponse response, JwtError error) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String errorMsg = String.format("{\"error\": \"%s\" }", error);

        response.getWriter().write(errorMsg);
    }
}
