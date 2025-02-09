package com.quickstart.kitchensink.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
    @NotBlank
    private String email;

    @Setter
    @NotBlank
    private String password;

}
