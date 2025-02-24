package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.application.UserApplicationService;
import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserApplicationService userApplicationService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO createdUser = userApplicationService.createUser(request);
        return ResponseEntity.created(URI.create("/api/users/" + createdUser.getUserId())).body(createdUser);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER_EDIT', 'USER_PROFILE_EDIT')")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserUpdateRequest userRequest) {
        UserDTO userDTO = userApplicationService.updateUser(userRequest);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER_DELETE')")
    public ResponseEntity<Void> deleteUser(@NotBlank @PathVariable String userId) {
        userApplicationService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyAuthority('USER_PROFILE_VIEW','USER_VIEW')")
    public ResponseEntity<UserDTO> getUserByEmailId(@NotBlank @PathVariable String email) {
        return ResponseEntity.ok(userApplicationService.getUserByEmail(email));
    }

    @GetMapping("/userType/{userType}")
    @PreAuthorize("hasAnyAuthority('USER_VIEW')")
    public ResponseEntity<List<UserDTO>> findAllUsers(@PathVariable UserType userType) {
        return ResponseEntity.ok(userApplicationService.findAllUsersByUserType(userType));
    }

    @PatchMapping("/{userId}/lock")
    @PreAuthorize("hasAnyAuthority('USER_LOCK')")
    public ResponseEntity<Void> lockUser(@NotBlank @PathVariable String userId) {
        userApplicationService.lockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/unlock")
    @PreAuthorize("hasAnyAuthority('USER_LOCK')")
    public ResponseEntity<Void> unlockUser(@NotBlank @PathVariable String userId) {
        userApplicationService.unlockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/roles")
    @PreAuthorize("hasAnyAuthority('USER_EDIT')")
    public ResponseEntity<Void> assignRolesToUser(@NotBlank @PathVariable String userId,
                                                  @RequestBody List<String> roles) {
        userApplicationService.assignRolesToUser(userId, roles);
        return ResponseEntity.noContent().build();
    }
}
