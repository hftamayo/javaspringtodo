package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.security.interfaces.UserInfoProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoProviderManager implements UserInfoProvider {
    private final UserService userService;

    @Override
    public UserDetails getUserDetails(String username) {
        return userService.getUserByEmail(username)
                .orElseThrow(() -> new
                        UsernameNotFoundException(
                        "Invalid Credentials: Username or Password not found"));
    }
}
