package com.quickstart.kitchensink.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    String token;
    long expiresIn;
    String email;

    public static LoginResponse of(String token, long expiresIn, String email) {
        return LoginResponse.builder()
                .token(token)
                .email(email)
                .expiresIn(expiresIn)
                .build();
    }
}
