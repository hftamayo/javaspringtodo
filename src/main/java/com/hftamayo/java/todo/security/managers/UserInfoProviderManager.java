package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.security.interfaces.UserInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoProviderManager implements UserInfoProvider {
    private final @Lazy UserDetailsService userDetailsService;

    @Override
    public UserDetails getUserDetails(String username) {
        return userDetailsService.loadUserByUsername(username);
    }
}
