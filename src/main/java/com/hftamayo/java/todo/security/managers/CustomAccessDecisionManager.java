package com.hftamayo.java.todo.security.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();

        logger.info("Checking access for URL: " + url);
        logger.info("authentication: "+ authentication);
        logger.info("configAttributes: "+ configAttributes);

        if (url.startsWith("/api/auth/login") || url.startsWith("/api/auth/register")) {
            // Allow the request to proceed if it's for the register or login workflows
            return;
        }

        logger.info("entering to the requiredRoles loop");

//        Set<String> requiredRoles = configAttributes.stream()
//                .map(ConfigAttribute::getAttribute)
//                .peek(attribute -> logger.info("Before flatMap, attribute: " + attribute))
//                .filter(Objects::nonNull)
//                .flatMap(attribute -> {
//                    logger.info("Inside flatMap, processing attribute: " + attribute);
//                    Pattern pattern = Pattern.compile("hasAnyRole\\((.*?)\\)");
//                    Matcher matcher = pattern.matcher(attribute);
//                    if (!matcher.find()) {
//                        logger.info("No match found for attribute: " + attribute);
//                        return Stream.empty();
//                    }
//                    do {
//                        String rolesString = matcher.group(1);
//                        logger.info("Extracted role: " + rolesString);
//                        String[] roles = rolesString.replace("'", "").split(",");
//                        return Arrays.stream(roles);
//                    } while (matcher.find());
//                })
//                .peek(role -> logger.info("After flatMap, role: " + role))
//                .collect(Collectors.toSet());

//        Set<String> requiredRoles = configAttributes.stream()
//                .map(ConfigAttribute::getAttribute)
//                .filter(Objects::nonNull)
//                .map(attribute -> attribute.replace("hasAnyRole(", "").replace(")", ""))
//                .map(rolesString -> rolesString.replace("'", "").split(","))
//                .flatMap(Arrays::stream)
//                .collect(Collectors.toSet());

        Set<String> requiredRoles = configAttributes.stream()
                .peek(attribute -> logger.info("Before map, attribute: " + attribute))
                .map(ConfigAttribute::getAttribute)
                .peek(attribute -> logger.info("After map, attribute: " + attribute))
                .collect(Collectors.toSet());

        logger.info("Required roles: " + requiredRoles);

        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        logger.info("User roles: " + userRoles);

        // Check if userRoles contains any of the requiredRoles
        if (Collections.disjoint(userRoles, requiredRoles)) {
            throw new AccessDeniedException("Access Denied: Required roles: " + requiredRoles + ", but given roles: " + userRoles);
        }
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}