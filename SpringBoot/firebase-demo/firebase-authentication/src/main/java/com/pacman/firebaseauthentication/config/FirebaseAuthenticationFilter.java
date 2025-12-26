package com.pacman.firebaseauthentication.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Get Authorization header
        String header = request.getHeader("Authorization");

        // 2️⃣ Check if header exists and starts with Bearer
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // remove "Bearer " prefix
            try {
                // 3️⃣ Verify token using Firebase Admin SDK
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                // 4️⃣ Check if the email is verified
                if (!decodedToken.isEmailVerified()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                    response.getWriter().write("Email not verified. Please verify your Gmail first.");
                    return; // Stop request processing
                }

                // 5️⃣ Extract user details
                String uid = decodedToken.getUid();
                String email = decodedToken.getEmail();
                String name = decodedToken.getName();
                String picture = decodedToken.getPicture();

                // 6️⃣ Log user info (optional)
                System.out.println("Authenticated & verified user: " + email + " (" + name + ")");

                // 7️⃣ Create Spring Security Authentication token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());

                // 8️⃣ Attach additional request details
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 9️⃣ Store authentication in Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (FirebaseAuthException e) {
                // 🔟 Invalid token → return 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        // 1️⃣1️⃣ Continue request chain
        filterChain.doFilter(request, response);
    }
}
