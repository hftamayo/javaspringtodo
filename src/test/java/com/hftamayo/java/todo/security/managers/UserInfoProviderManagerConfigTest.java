package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserInfoProviderManagerConfigTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserInfoProviderManagerConfig userInfoProviderManagerConfig;

    @Test
    void createUserInfoProviderManager_ShouldReturnValidManager() {
        // Act
        UserInfoProviderManager manager = userInfoProviderManagerConfig.createUserInfoProviderManager();

        // Assert
        assertAll(
            () -> assertNotNull(manager),
            () -> assertInstanceOf(UserInfoProviderManager.class, manager)
        );
    }
}