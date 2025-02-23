package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.application.AuthApplicationService;
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
    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public AuthResponseDTO authenticate(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return authApplicationService.authenticate(authRequestDTO);
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasAnyAuthority('USER_RESET_PASSWORD')")
    public ResponseEntity<?> authenticate(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authApplicationService.authenticate(passwordResetRequest);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/reset-password/{email}")
    @PreAuthorize("hasAnyAuthority('USER_RESET_PASSWORD')")
    public ResponseEntity<?> resetPassword(@NotBlank @PathVariable String email) {
        authApplicationService.resetPassword(email);
        return ResponseEntity.accepted().build();
    }
}