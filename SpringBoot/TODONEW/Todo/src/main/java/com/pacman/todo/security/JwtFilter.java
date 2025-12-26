package com.pacman.todo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("JWT Filter processing: " + request.getMethod() + " " + path);

        // Allow auth endpoints and health endpoint without JWT
        if (path.startsWith("/api/auth") || path.equals("/api/health")) {
            System.out.println("Allowing unauthenticated access to: " + path);
            filterChain.doFilter(request, response);
            return;
        }
        // Allow preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("Allowing OPTIONS request");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token extracted, length: " + token.length());

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                System.out.println("Token validation failed");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"error\": \"Unauthorized or invalid token\", \"debug\": \"Token validation failed in JwtFilter\"}");
                return;
            }

            // Store username in request for controller access
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                System.out.println("Token validated for user: " + username);
                request.setAttribute("username", username);

                // Set Spring Security authentication context most valuable lines
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
                        new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                System.out.println("Error extracting username from token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
                return;
            }
        } else {
            // No token provided for protected endpoint
            System.out.println("No token provided for protected endpoint");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing JWT token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
