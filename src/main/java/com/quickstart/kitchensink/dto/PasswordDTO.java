package com.quickstart.kitchensink.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PasswordDTO {
    private String oldPassword;
    private String newPassword;

    public static PasswordDTO of(String oldPassword, String newPassword) {
        return PasswordDTO.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
    }
}
