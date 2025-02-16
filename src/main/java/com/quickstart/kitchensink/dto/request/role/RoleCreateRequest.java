package com.quickstart.kitchensink.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {
    @NotBlank
    @Size(max = 15, message = "Password must not exceed 15 characters")
    private String roleName;

    @Size(max = 100, message = "Password must not exceed 100 characters")
    private String roleDescription;
}
