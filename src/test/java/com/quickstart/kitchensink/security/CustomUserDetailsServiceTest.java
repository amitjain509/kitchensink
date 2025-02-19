package com.quickstart.kitchensink.security;

import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(user.isPasswordResetRequired()).thenReturn(false);
        when(user.isActive()).thenReturn(true);
        when(user.isLocked()).thenReturn(false);

        UserDetails result = customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
        when(userService.getUserByEmail("unknown@example.com")).thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("unknown@example.com"));
    }

    @Test
    void loadUserByUsername_WhenUserIsInactive_ShouldThrowDisabledException() {
        when(userService.getUserByEmail("inactive@example.com")).thenReturn(user);
        when(user.isPasswordResetRequired()).thenReturn(false);
        when(user.isActive()).thenReturn(false);

        assertThrows(DisabledException.class, () -> customUserDetailsService.loadUserByUsername("inactive@example.com"));
    }

    @Test
    void loadUserByUsername_WhenUserIsLocked_ShouldThrowLockedException() {
        when(userService.getUserByEmail("locked@example.com")).thenReturn(user);
        when(user.isPasswordResetRequired()).thenReturn(false);
        when(user.isActive()).thenReturn(true);
        when(user.isLocked()).thenReturn(true);

        assertThrows(LockedException.class, () -> customUserDetailsService.loadUserByUsername("locked@example.com"));
    }

    @Test
    void loadUserByUsername_WhenPasswordResetRequired_ShouldReturnUser() {
        when(userService.getUserByEmail("reset@example.com")).thenReturn(user);
        when(user.isPasswordResetRequired()).thenReturn(true);

        UserDetails result = customUserDetailsService.loadUserByUsername("reset@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }
}
