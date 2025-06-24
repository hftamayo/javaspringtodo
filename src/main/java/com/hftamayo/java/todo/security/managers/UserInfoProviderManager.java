package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.security.interfaces.UserInfoProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoProviderManager implements UserInfoProvider {
    private final UserService userService;

    @Override
    public UserDetails getUserDetails(String email) {
        User user = userService.loginRequest(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials: Email or Password not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getAuthorities()
        );
    }
}
