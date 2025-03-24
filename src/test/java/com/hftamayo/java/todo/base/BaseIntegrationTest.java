package com.hftamayo.java.todo.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("integration")
public abstract class BaseIntegrationTest {
    // Context loaded once with MySQL properties
}
