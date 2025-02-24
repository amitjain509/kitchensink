package com.quickstart.kitchensink.application;

import com.quickstart.kitchensink.dto.request.AuthRequestDTO;
import com.quickstart.kitchensink.dto.request.user.PasswordResetRequest;
import com.quickstart.kitchensink.dto.response.AuthResponseDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.enums.AuthenticationType;
import com.quickstart.kitchensink.security.jwt.JwtService;
import com.quickstart.kitchensink.security.service.AuthenticationFactory;
import com.quickstart.kitchensink.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthenticationFactory authFactory;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password123";
    private final String encodedPassword = "encodedPassword123";
    private final String jwtToken = "jwtToken123";

    @BeforeEach
    void setUp() {
        lenient().when(passwordEncoder.encode(any())).thenReturn(encodedPassword);
    }

    @Test
    void authenticate_Success() {
        AuthRequestDTO authRequest = new AuthRequestDTO(testEmail, testPassword);
        User user = Mockito.mock(User.class);
        when(userService.getUserByEmail(testEmail)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthResponseDTO response = authApplicationService.resetPassword(authRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        verify(authFactory).authenticate(AuthenticationType.SPRING_SECURITY, testEmail, testPassword);
        verify(userService).getUserByEmail(testEmail);
        verify(jwtService).generateToken(user);
    }

    @Test
    void authenticate_InvalidCredentials() {
        AuthRequestDTO authRequest = new AuthRequestDTO(testEmail, testPassword);
        when(userService.getUserByEmail(testEmail)).thenThrow(KitchenSinkException.builder(ApplicationErrorCode.INVALID_CREDENTIALS).build());

        KitchenSinkException exception = assertThrows(KitchenSinkException.class, () -> authApplicationService.resetPassword(authRequest));
        assertEquals(ApplicationErrorCode.INVALID_CREDENTIALS, exception.getApplicationErrorCode());
    }

    @Test
    void authenticate_PasswordReset_Success() {
        PasswordResetRequest resetRequest = new PasswordResetRequest(testEmail, testPassword, "newPassword");

        authApplicationService.resetPassword(resetRequest);

        verify(authFactory).authenticate(AuthenticationType.SPRING_SECURITY, testEmail, testPassword);
        verify(userService).updatePassword(eq(testEmail), anyString());
    }

    @Test
    void resetPassword_Success() {
        authApplicationService.resetPassword(testEmail);
        verify(userService).resetPassword(testEmail, encodedPassword);
    }
}
