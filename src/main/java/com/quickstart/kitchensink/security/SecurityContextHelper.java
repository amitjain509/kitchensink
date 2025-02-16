package com.quickstart.kitchensink.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityContextHelper {

    // Get the current CustomAuthenticationToken (if available)
    public static CustomAuthenticationToken getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            return (CustomAuthenticationToken) authentication;
        }
        return null;
    }

    // Access the userType directly
    public static String getCurrentUserType() {
        CustomAuthenticationToken auth = getCurrentAuthentication();
        return (auth != null) ? auth.getUserType() : null;
    }

    // Access the permissions directly
    public static List<String> getCurrentPermissions() {
        CustomAuthenticationToken auth = getCurrentAuthentication();
        return (auth != null) ? auth.getPermissions() : null;
    }
}
