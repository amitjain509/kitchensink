package com.quickstart.kitchensink.security.providers;

import com.quickstart.kitchensink.security.enums.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringSecurityAuthProvider implements AuthenticationProvider {

    private final AuthenticationManager authManager;

    @Override
    public UserDetails authenticate(String username, String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public AuthenticationType getAuthenticationType() {
        return AuthenticationType.SPRING_SECURITY;
    }
}
