package com.quickstart.kitchensink.security.providers;

import com.quickstart.kitchensink.security.enums.AuthenticationType;

public interface AuthenticationProvider {
    Object authenticate(String username, String password);

    AuthenticationType getAuthenticationType();
}