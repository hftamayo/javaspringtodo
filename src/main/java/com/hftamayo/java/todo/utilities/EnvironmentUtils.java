package com.hftamayo.java.todo.utilities;

import org.springframework.core.env.Environment;

public class EnvironmentUtils {
    private Environment environment;

    public EnvironmentUtils(Environment environment) {
        this.environment = environment;
    }

    public boolean isDevProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equals("testing") || profile.equals("development")) {
                return true;
            }
        }
        return false;
    }
}
