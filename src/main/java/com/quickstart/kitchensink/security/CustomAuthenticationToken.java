package com.quickstart.kitchensink.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String userType;
    private final List<String> permissions;

    public CustomAuthenticationToken(UserDetails principal, Object credentials, List<String> permissions, String userType) {
        super(principal, credentials, principal.getAuthorities());
        this.permissions = permissions;
        this.userType = userType;
    }
}
