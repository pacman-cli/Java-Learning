package com.pacman.uberprojectauthservice.configurations;

import com.pacman.uberprojectauthservice.services.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SpringSecurity implements WebMvcConfigurer {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImp();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //Strategy
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Now the default chain is gone, and we haven't defined any custom filters. But
    // by
    // default in the filter chain, CSRF is enabled.
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // We aren’t using the CSRF token anymore, so let’s disable it
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/v1/auth/signup/**")
                                .permitAll()
                                .requestMatchers("/api/v1/auth/signing/**")
                                .permitAll())
                .build();
        // ant matchers will allow us some of the requests bypassed
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) { //this is from WebMvcConfigurer
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}
