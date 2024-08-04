package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.interfaces.UserInfoProvider;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserInfoProviderManager userInfoProviderManager;
    private final CustomTokenProvider customTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        logger.info("Request path: " + path);
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = getTokenFromRequest(request);
            final String username;

            if (token == null) {
                logger.info("No token found in request");
                filterChain.doFilter(request, response);
                return;
            }

            username = customTokenProvider.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userInfoProviderManager.getUserDetails(username);
                if (customTokenProvider.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("User authenticated successfully");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error in AuthenticationFilter: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("message from Authentication Filter workflow: Unauthorized access");
            response.getWriter().write("A problem occurred during the authentication process. Please try again.");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}