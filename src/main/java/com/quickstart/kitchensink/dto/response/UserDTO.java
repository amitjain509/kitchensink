package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.enums.UserType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class UserDTO {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean active;
    private boolean locked;
    private boolean isPasswordResetRequired;
    private UserType userType;
    private List<BasicRoleDTO> roles;
    private Set<String> permissions;
}
