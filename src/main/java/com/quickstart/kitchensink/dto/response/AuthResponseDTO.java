package com.quickstart.kitchensink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String userType;
    private List<String> permissions;
}
