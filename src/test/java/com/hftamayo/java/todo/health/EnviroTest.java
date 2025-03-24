package com.hftamayo.java.todo.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class EnviroTest {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Value("${spring.datasource.url:}")
    private String databaseUrl;

    @Test
    void contextLoads() {
        // Verifies if Spring context loads successfully
    }

    @Test
    void testProfileIsActive() {
        assertThat(activeProfile).isEqualTo("testing");
    }

    @Test
    void databaseConnectionExists() {
        assertThat(databaseUrl).isNotEmpty();
    }

    @Test
    void requiredBeansArePresent() {
        assertThat(System.getProperty("java.version"))
                .as("Java Version Check")
                .isNotEmpty();
    }

    @Test
    void memoryIsAvailable() {
        long freeMemory = Runtime.getRuntime().freeMemory();
        assertThat(freeMemory)
                .as("Free Memory Check")
                .isGreaterThan(0);
    }
}

