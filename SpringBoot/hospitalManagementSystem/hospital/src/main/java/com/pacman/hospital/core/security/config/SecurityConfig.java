package com.pacman.hospital.core.security.config;

import com.pacman.hospital.core.security.filter.JwtAuthenticationFilter;
import com.pacman.hospital.core.security.service.UserDetailsServiceImpl;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
            new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth ->
                auth
                    // Public endpoints
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                    )
                    .permitAll()
                    .requestMatchers("/actuator/**")
                    .permitAll()
                    // Patient endpoints - accessible by patients and staff
                    .requestMatchers("/api/patients/**")
                    .hasAnyRole(
                        "PATIENT",
                        "DOCTOR",
                        "NURSE",
                        "ADMIN",
                        "RECEPTIONIST"
                    )
                    // Doctor endpoints
                    // Allow read-only access to doctors for patients and staff (needed for
                    // scheduling)
                    .requestMatchers(HttpMethod.GET, "/api/doctors/**")
                    .hasAnyRole(
                        "DOCTOR",
                        "ADMIN",
                        "DEPARTMENT_HEAD",
                        "PATIENT",
                        "RECEPTIONIST",
                        "NURSE"
                    )
                    // Write access remains restricted
                    .requestMatchers(HttpMethod.POST, "/api/doctors/**")
                    .hasAnyRole("DOCTOR", "ADMIN", "DEPARTMENT_HEAD")
                    .requestMatchers(HttpMethod.PUT, "/api/doctors/**")
                    .hasAnyRole("DOCTOR", "ADMIN", "DEPARTMENT_HEAD")
                    .requestMatchers(HttpMethod.DELETE, "/api/doctors/**")
                    .hasAnyRole("DOCTOR", "ADMIN", "DEPARTMENT_HEAD")
                    // Appointment endpoints - accessible by patients, doctors, and staff
                    .requestMatchers("/api/appointments/**")
                    .hasAnyRole(
                        "PATIENT",
                        "DOCTOR",
                        "NURSE",
                        "ADMIN",
                        "RECEPTIONIST"
                    )
                    // Pharmacy/Prescription endpoints - accessible by pharmacists, doctors, admin, and patients
                    .requestMatchers("/api/prescriptions/**")
                    .hasAnyRole("PHARMACIST", "DOCTOR", "ADMIN", "PATIENT")
                    .requestMatchers("/api/medicines/**")
                    .hasAnyRole("PHARMACIST", "DOCTOR", "ADMIN", "PATIENT")
                    // Laboratory endpoints - accessible by lab technicians, doctors, admin, and patients
                    .requestMatchers("/api/lab-orders/**")
                    .hasAnyRole("LAB_TECHNICIAN", "DOCTOR", "ADMIN", "PATIENT")
                    .requestMatchers("/api/lab-tests/**")
                    .hasAnyRole("LAB_TECHNICIAN", "DOCTOR", "ADMIN", "PATIENT")
                    // Billing endpoints - accessible by billing staff, admin, and patients (for their own data)
                    .requestMatchers("/api/billings/**")
                    .hasAnyRole("BILLING_STAFF", "ADMIN", "PATIENT")
                    // Insurance endpoints - accessible by insurance staff, admin, and patients
                    .requestMatchers("/api/insurance/**")
                    .hasAnyRole("INSURANCE_STAFF", "ADMIN", "PATIENT")
                    // Documents endpoints - accessible by staff and patients
                    .requestMatchers("/api/documents/**")
                    .hasAnyRole("DOCTOR", "NURSE", "ADMIN", "PATIENT")
                    // Medical records - accessible by doctors, nurses, admin, and patients (for their own data)
                    .requestMatchers("/api/medical-records/**")
                    .hasAnyRole("DOCTOR", "NURSE", "ADMIN", "PATIENT")
                    // Payment endpoints - accessible by billing staff and admin
                    .requestMatchers("/api/payments/**")
                    .hasAnyRole("BILLING_STAFF", "ADMIN")
                    // Admin endpoints - only accessible by admin
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")
                    // All other requests need authentication
                    .anyRequest()
                    .authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
