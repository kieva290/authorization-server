package com.bcx.auth.backend.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    /**
     * This filter indicates that all the incoming requests must be authorized,
     * to obtain access to the endpoints, a redirection to the login page will be done,
     * the OAuth2 configuration is done in the application.yml
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login.loginPage(OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/messages-client-oidc"
                ))
                .oauth2Login(Customizer.withDefaults());
        return http.build();
    }
}
