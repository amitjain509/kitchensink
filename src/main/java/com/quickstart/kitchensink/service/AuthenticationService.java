package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.request.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.quickstart.kitchensink.model.Member;

import javax.security.sasl.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberRegistrationService memberRegistrationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberRequest input) throws Exception {
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        memberRegistrationService.register(input);
    }

    public void authenticate(MemberRequest input) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        Member member = memberRegistrationService.getMemberByEmail(input.getEmail());

        if (!passwordEncoder.matches(input.getPassword(), member.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}