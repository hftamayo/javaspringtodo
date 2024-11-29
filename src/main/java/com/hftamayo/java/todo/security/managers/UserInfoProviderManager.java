package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.security.interfaces.UserInfoProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Service
public class UserInfoProviderManager implements UserInfoProvider {
    private final UserService userService;

    @Override
    public UserDetails getUserDetails(String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> new
                        UsernameNotFoundException(
                        "Invalid Credentials: Email or Password not found"));
    }
}
