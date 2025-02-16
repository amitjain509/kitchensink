package com.quickstart.kitchensink.security;

import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);

        if (!user.isActive()) {
            throw new DisabledException("User account is not active.");
        }

        if (user.isLocked()) {
            throw new LockedException("User account is locked.");
        }

        return user;
    }
}
