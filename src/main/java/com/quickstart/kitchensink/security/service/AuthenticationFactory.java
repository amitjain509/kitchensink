package com.quickstart.kitchensink.security.service;

import com.quickstart.kitchensink.security.enums.AuthenticationType;
import com.quickstart.kitchensink.security.providers.AuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthenticationFactory {

    private final Map<AuthenticationType, AuthenticationProvider> authProviderMap;

    public AuthenticationFactory(List<AuthenticationProvider> authenticationProviderList) {
        this.authProviderMap = authenticationProviderList.stream()
                .collect(Collectors.toMap(AuthenticationProvider::getAuthenticationType, Function.identity()));
    }

    public Object authenticate(AuthenticationType authType, String username, String password) {
        return Optional.ofNullable(authProviderMap.get(authType))
                .map(provider -> provider.authenticate(username, password))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported auth type: " + authType));

    }
}