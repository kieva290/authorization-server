package com.bcx.auth.backend.resources.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    /**
     *  For the backend-resources, indicate that all the end-points are protected,
     *  to request any end-point, the OAuth2 protocol is necessary, using the server configured and with the given scope,
     *  thus, a JWT will be used to communicate between the backend-resources and backend-auth when backend-resources
     *  needs to validate the authentication of a request
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/**")
                .access("hasAuthority('SCOPE_message.read')")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

}
