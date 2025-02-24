package com.quickstart.kitchensink.security.jwt;

import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.security.service.CustomUserDetailsService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @InjectMocks
    private JwtService jwtService; // Real JwtService, no Mock

    @Mock
    private CustomUserDetailsService userDetailsService;

    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private UserDetails mockUserDetails;
    private String validToken;
    private String expiredToken;
    User testUser;
    String key = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());

    @BeforeEach
    void setUp() throws Exception {
        jwtRequestFilter = new JwtRequestFilter(jwtService, userDetailsService);
        // Set the secret key & expiration time manually

        setPrivateField(jwtService, "secretKey", key);
        setPrivateField(jwtService, "jwtExpiration", 3600000L); // 1 hour

        // Mock user details
        mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("password")
                .authorities("USER_VIEW")
                .build();

        // Create test User for token generation
        testUser = User.builder().id("1")
                .name("testUser")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .active(true)
                .userType(UserType.USER)
                .roles(List.of(Role.builder().name("USER_ROLE").build()))
                .build();


        // Generate a real JWT token
        validToken = jwtService.generateToken(testUser);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(mockUserDetails);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername("test@example.com");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testNoAuthorizationHeader_ShouldContinueFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(userDetailsService);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
