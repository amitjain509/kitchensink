package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponseDTO authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));

        User user = userService.getUserByEmail(authRequestDTO.getEmail());
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.from(user, token, user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
    }

    @PostMapping("/reset-password")
    public AuthResponseDTO authenticate(@RequestBody PasswordResetRequest passwordResetRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                passwordResetRequest.getEmail(),
                passwordResetRequest.getOldPassword()));

        User user = userService.resetPassword(passwordResetRequest.getEmail(),
                passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.from(user, token, user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
    }
}