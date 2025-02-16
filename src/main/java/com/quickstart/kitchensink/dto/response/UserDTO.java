package com.quickstart.kitchensink.dto.response;

import com.quickstart.kitchensink.enums.UserType;
import lombok.Builder;
import lombok.Getter;

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
    private UserType userType;
}
