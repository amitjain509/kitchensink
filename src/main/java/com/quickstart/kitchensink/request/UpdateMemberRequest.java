package com.quickstart.kitchensink.request;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequest {

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^(\\+91[\\s-]?)?[6-9]\\d{9}$", message = "Invalid Indian phone number format")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 15, message = "Password must not exceed 15 characters")
    private String password;
}
