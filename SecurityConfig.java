package com.example.Spring_Sec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private User_Details_Service user_details_service; // Ensure this is a valid bean

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/register", "/api/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginProcessingUrl("/api/login")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write("Login successful");
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Login failed");
                })
            )
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write("Logout successful");
                })
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080","http://127.0.0.1:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Expose AuthenticationManager as a bean
    }
}