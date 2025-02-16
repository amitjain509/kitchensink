package com.quickstart.kitchensink.dto.request.role;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionUpdateRequest {
    @NotBlank
    @Size(max = 15, message = "Password must not exceed 15 characters")
    private String roleName;

    private List<String> permissions;
}
