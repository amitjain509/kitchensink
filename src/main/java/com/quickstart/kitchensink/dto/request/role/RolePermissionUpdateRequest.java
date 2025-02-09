package com.quickstart.kitchensink.dto.request.role;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionUpdateRequest {
    @NotBlank
    private String roleId;

    private List<String> permissions;
}
