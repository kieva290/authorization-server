package com.bcx.auth.backend.client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final WebClient webClient;

    /**
     * To request this endpoint, we indicate which OAuth2 client will be used,
     * then, a request to the backend-resources is done uwing the correct client and filling the request with the
     * given OAuth2 client,
     * the input parameter OAuth2AuthorizedClient is filled/validated with requests against backend-auth
     */
    @GetMapping("/messages")
    public String getMessages(@RegisteredOAuth2AuthorizedClient("messages-client-authorization-code")
                              OAuth2AuthorizedClient authorizedClient) {
        return webClient.get()
                .uri("http://backend-resouces:8082/messages")
                .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
