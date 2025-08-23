package com.pacman.uberprojectauthservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
  @Bean // Now the default chain is gone and we haven't defined any custom filters.    But by
        // default in the filter chain, CSRF is enabled.
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    //    We aren’t using the CSRF token anymore, so let’s disable it
      return httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/api/v1/auth/signup/**").permitAll()
                        .requestMatchers("/api/v1/auth/signing/**").permitAll()

        ).build();
    // ant matchers will allow us some of the requests bypassed
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
