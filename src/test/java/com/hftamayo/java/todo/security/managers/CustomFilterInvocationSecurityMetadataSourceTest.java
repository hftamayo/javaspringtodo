package com.hftamayo.java.todo.security.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomFilterInvocationSecurityMetadataSourceTest {

    @InjectMocks
    private CustomFilterInvocationSecurityMetadataSource metadataSource;

    @Mock
    private FilterInvocation filterInvocation;

    @Test
    void getAttributes_PublicEndpoints_ShouldReturnAnonymousRole() {
        // Arrange
        List<String> publicUrls = List.of(
                "/api/auth/login",
                "/api/auth/register",
                "/api/health"
        );

        for (String url : publicUrls) {
            when(filterInvocation.getRequestUrl()).thenReturn(url);

            // Act
            Collection<ConfigAttribute> attributes = metadataSource.getAttributes(filterInvocation);

            // Assert
            assertAll(
                    () -> assertNotNull(attributes),
                    () -> assertEquals(1, attributes.size()),
                    () -> assertEquals("ROLE_ANONYMOUS", attributes.iterator().next().getAttribute())
            );
        }
    }

    @Test
    void getAttributes_SecuredEndpoint_ShouldReturnUserRoles() {
        // Arrange
        when(filterInvocation.getRequestUrl()).thenReturn("/api/secured/endpoint");

        // Act
        Collection<ConfigAttribute> attributes = metadataSource.getAttributes(filterInvocation);

        // Assert
        assertAll(
                () -> assertNotNull(attributes),
                () -> assertEquals(3, attributes.size()),
                () -> assertTrue(attributes.stream()
                        .map(ConfigAttribute::getAttribute)
                        .collect(Collectors.toSet())
                        .containsAll(Set.of("ROLE_USER", "ROLE_SUPERVISOR", "ROLE_ADMIN")))
        );
    }

    @Test
    void supports_FilterInvocationClass_ShouldReturnTrue() {
        assertTrue(metadataSource.supports(FilterInvocation.class));
    }

    @Test
    void supports_OtherClass_ShouldReturnFalse() {
        assertFalse(metadataSource.supports(String.class));
    }

    @Test
    void getAllConfigAttributes_ShouldReturnNull() {
        assertNull(metadataSource.getAllConfigAttributes());
    }
}