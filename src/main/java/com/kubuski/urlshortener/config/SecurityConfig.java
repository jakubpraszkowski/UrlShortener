package com.kubuski.urlshortener.config;

import com.kubuski.urlshortener.entity.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_UI_PATHS = {
            "/swagger-ui/**",
            "/v3/api-docs*/**",
            "/swagger/**"
    };

    private static final String[] SHORTEN_PERMIT_PATHS = {
            "/api/v1/shorten/**"
    };

    private static final String[] USER_PERMIT_PATHS = {
            "/api/v1/users/**"
    };

    private static final String[] URL_PERMIT_PATHS = {
            "/api/v1/url/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SHORTEN_PERMIT_PATHS).hasAnyRole(String.valueOf(Roles.USER), String.valueOf(Roles.ADMIN))
                        .requestMatchers(SWAGGER_UI_PATHS).permitAll()
                        .requestMatchers(USER_PERMIT_PATHS).hasRole(String.valueOf(Roles.ADMIN))
                        .requestMatchers(URL_PERMIT_PATHS).hasRole(String.valueOf(Roles.USER))
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
