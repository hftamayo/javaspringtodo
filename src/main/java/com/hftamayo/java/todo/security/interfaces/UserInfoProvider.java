package com.hftamayo.java.todo.security.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserInfoProvider {
    UserDetails getUserDetails(String email);
}
