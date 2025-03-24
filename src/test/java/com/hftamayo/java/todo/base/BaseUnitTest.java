package com.hftamayo.java.todo.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("testing")
public abstract class BaseUnitTest {
    // Context loaded once with H2 properties
}