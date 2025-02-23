package com.quickstart.kitchensink.application;

import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.password}")
    private String defaultPassword;

    @Transactional
    public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));

        try {
            User user = userService.getUserByEmail(authRequestDTO.getEmail());
            String token = jwtService.generateToken(user);
            return AuthResponseDTO.from(user, token);
        } catch (KitchenSinkException e) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.INVALID_CREDENTIALS)
                    .referenceId(authRequestDTO.getEmail())
                    .build();
        }
    }

    @Transactional
    public void authenticate(PasswordResetRequest passwordResetRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                passwordResetRequest.getEmail(),
                passwordResetRequest.getOldPassword()));

        userService.updatePassword(passwordResetRequest.getEmail(),
                passwordEncoder.encode(passwordResetRequest.getNewPassword()));
    }

    @Transactional
    public void resetPassword(String email) {
        String defaultPasswordHash = passwordEncoder.encode(defaultPassword);
        userService.resetPassword(email, defaultPasswordHash);
    }
}
