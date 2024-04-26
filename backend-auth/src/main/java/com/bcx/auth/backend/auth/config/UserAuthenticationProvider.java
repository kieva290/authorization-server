package com.bcx.auth.backend.auth.config;

import com.bcx.auth.backend.auth.entities.AuthUser;
import com.bcx.auth.backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.Optional;

/**
 * Provider which will validate the Authentication object present in the SecurityContext,
 * the only acceptable Authentication object is the UsernamePasswordAuthenticationToken which comes from the,
 * UserAuthenticationConverter, then from the username and password present in the Authentication object,
 * validate the information against the database
 * if the username and password don't match with the data present in the database, null is returned as the
 * Authentication object in the SecurityContext
 */
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<AuthUser> oUser = userRepository.findByLogin(login);

        if (oUser.isEmpty()) {
            return null;
        }

        AuthUser authUser = oUser.get();

        if (passwordEncoder.matches(CharBuffer.wrap(password), authUser.getPassword())) {
            return UsernamePasswordAuthenticationToken.authenticated(login, password, Collections.emptyList());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

}
