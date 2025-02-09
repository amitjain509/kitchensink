package com.quickstart.kitchensink.request;

import com.quickstart.kitchensink.validators.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^(\\+91[\\s-]?)?[6-9]\\d{9}$", message = "Invalid Indian phone number format")
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    String phoneNumber;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name;

    @UniqueEmail
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    String email;

    @Setter
    @NotBlank
    @Size(max = 15, message = "Password must not exceed 15 characters")
    String password;
}
