package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponseDTO authenticate(@RequestParam("email") String email, @RequestParam("password") String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = userService.getUserByEmail(email);
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, user.getUserType().name(), user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
    }
}