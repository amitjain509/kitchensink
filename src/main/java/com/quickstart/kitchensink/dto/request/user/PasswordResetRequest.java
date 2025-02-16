package com.quickstart.kitchensink.dto.request.user;

import com.quickstart.kitchensink.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    private String email;

    @Setter
    @NotBlank
    @Size(max = 15, message = "Password must not exceed 15 characters")
    private String oldPassword;

    @Setter
    @NotBlank
    @Size(max = 15, message = "Password must not exceed 15 characters")
    private String newPassword;

    private UserType userType;
}
