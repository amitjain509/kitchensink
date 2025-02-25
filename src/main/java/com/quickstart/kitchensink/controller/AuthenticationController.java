package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.application.AuthApplicationService;
import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public AuthResponseDTO authenticate(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return authApplicationService.resetPassword(authRequestDTO);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authApplicationService.resetPassword(passwordResetRequest);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/reset-password/{email}")
    @PreAuthorize("hasAnyAuthority('USER_RESET_PASSWORD')")
    public ResponseEntity<?> resetPassword(@NotBlank @PathVariable String email) {
        authApplicationService.resetPassword(email);
        return ResponseEntity.accepted().build();
    }
}