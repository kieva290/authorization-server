package com.bcx.auth.backend.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;

@EnableWebSecurity
public class SecurityConfig {

    /**
     *  first will be applied the OAuth2 security filters configuration,
     *  in this configuration, l only indicate that all the failing requests will be redirected to the /login page
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterFunction(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                // Redirect to the login page when not authorized from the
                // authorization endpoint
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")));
        return http.build();
    }

    /**
     * Then, will be applied the rest of the security filters,
     * here, l indicate that all the endpoints require authentication and which is the login form
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                //  form login handles the redirect to the login page from the
                //  authorization server filter chain
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * configuration of the OAuth2 client
     * multiple clients can be configured, the current implementation stores them in memory, but a table
     * in the database can be used to store all the registered clients
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // client-id and client-secret that must be used from all the OAuth2 clients
                .clientId("messages-client")
                .clientSecret("$2b$04$8BeFNn81bsjH2its9hStk.fNpLXMFyrrjSCaHmkJjU1tCt6qWU7Oq")
                // the Basic authentication method will be used between backend-client and backend-auth
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // grant types to be used
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // permitted redirect URI after the authentication is successful
                .redirectUri("http://backend-client:8083/login/oauth2/code/messages-client-oidc")
                .redirectUri("http://backend-client:8083/authorized")
                // acceptable scopes for the authorization
                .scope(OidcScopes.OPENID)
                .scope("message.read")
                .scope("message.write")
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     *  Acceptable URL of the authorization server
     */
    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://backend-auth:8081")
                .build();
    }

}
