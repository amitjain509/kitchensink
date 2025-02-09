package com.quickstart.kitchensink.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    String token;
    long expiresIn;

    public static LoginResponse of(String token, long expiresIn) {
        return LoginResponse.builder()
                .token(token)
                .expiresIn(expiresIn)
                .build();
    }
}
