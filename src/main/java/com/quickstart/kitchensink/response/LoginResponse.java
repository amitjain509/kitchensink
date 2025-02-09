package com.quickstart.kitchensink.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponse {
    String token;
    long expiresIn;
    String email;
    List<String> roles;

    public static LoginResponse of(String token, long expiresIn, String email, List<String> roles) {
        return LoginResponse.builder()
                .token(token)
                .email(email)
                .roles(roles)
                .expiresIn(expiresIn)
                .build();
    }
}
