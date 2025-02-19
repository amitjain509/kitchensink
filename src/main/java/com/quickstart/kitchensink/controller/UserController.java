package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO createdUser = userService.createUser(userMapper.fromCreateRequest(request));
        return ResponseEntity.created(URI.create("/api/users/" + createdUser.getUserId())).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateRequest userRequest) {
        userService.updateUser(userMapper.fromUpdateRequest(userRequest));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@NotBlank @PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmailId(@NotBlank @PathVariable String email) {
        UserDTO userDTO = UserMapper.fromEntity(userService.getUserByEmail(email));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/userType/{userType}")
    public ResponseEntity<List<UserDTO>> findAllUsers(@PathVariable UserType userType) {
        return ResponseEntity.ok(userService.findAllUsersByUserType(userType));
    }

    @PatchMapping("/{userId}/lock")
    public ResponseEntity<Void> lockUser(@NotBlank @PathVariable String userId) {
        userService.lockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/unlock")
    public ResponseEntity<Void> unlockUser(@NotBlank @PathVariable String userId) {
        userService.unlockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/roles")
    public ResponseEntity<Void> assignRolesToUser(@NotBlank @PathVariable String userId,
                                                  @RequestBody List<String> roles) {
        userService.assignRolesToUser(userId, roles);
        return ResponseEntity.noContent().build();
    }
}
