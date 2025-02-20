package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponseDTO authenticate(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));

        User user = userService.getUserByEmail(authRequestDTO.getEmail());
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.from(user, token);
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasAnyAuthority('USER_RESET_PASSWORD')")
    public AuthResponseDTO authenticate(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                passwordResetRequest.getEmail(),
                passwordResetRequest.getOldPassword()));

        User user = userService.updatePassword(passwordResetRequest.getEmail(),
                passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.from(user, token);
    }

    @PutMapping("/reset-password/{email}")
    @PreAuthorize("hasAnyAuthority('USER_RESET_PASSWORD')")
    public ResponseEntity<?> resetPassword(@NotBlank @PathVariable String email) {
        userService.resetPassword(email);
        return ResponseEntity.accepted().build();
    }
}