package com.spring.security.config;

import javax.management.relation.Role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.spring.security.filter.JwtAuthencationFilter;
import com.spring.security.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserService userService;
    private JwtAuthencationFilter jwtAuthencationFilter;

    public SecurityConfig(UserService userService, JwtAuthencationFilter jwtAuthencationFilter) {
        this.userService = userService;
        this.jwtAuthencationFilter = jwtAuthencationFilter;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            req -> req.requestMatchers("/login/**","/register/**","/h2-console/**")
                    .permitAll()
                    .requestMatchers("/success-admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest()
                    .authenticated()
        )
        .headers(headers -> headers
            .frameOptions().sameOrigin() // allow H2 console in iframe
        )
        .userDetailsService(userService)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthencationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    } 
}
