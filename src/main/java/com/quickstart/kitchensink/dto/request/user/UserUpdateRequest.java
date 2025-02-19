package com.quickstart.kitchensink.dto.request.user;

import com.quickstart.kitchensink.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^(\\[\\s-]?)?[6-9]\\d{9}$", message = "Invalid Indian phone number format")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String email;

    private UserType userType;
}
