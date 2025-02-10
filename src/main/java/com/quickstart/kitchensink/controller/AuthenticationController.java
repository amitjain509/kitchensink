package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.request.MemberRequest;
import com.quickstart.kitchensink.response.LoginResponse;
import com.quickstart.kitchensink.service.AuthenticationService;
import com.quickstart.kitchensink.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    ResponseEntity<String> addMember(@Valid @RequestBody MemberRequest memberRequest) {
        try {
            authenticationService.signup(memberRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody MemberRequest memberRequest) throws AuthenticationException {
        authenticationService.authenticate(memberRequest);

        String jwtToken = jwtService.generateToken(memberRequest);

        LoginResponse loginResponse = LoginResponse.of(jwtToken, jwtService.getExpirationTime(), memberRequest.getEmail());

        return ResponseEntity.ok(loginResponse);
    }
}