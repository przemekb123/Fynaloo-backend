package com.fynaloo.Configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public
class SecurityConfigTest {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // w testach uproszczenie
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

}