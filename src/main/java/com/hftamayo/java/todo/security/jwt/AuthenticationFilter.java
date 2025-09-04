package com.hftamayo.java.todo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.error.ErrorResponseDto;
import com.hftamayo.java.todo.exceptions.AuthenticationException;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserInfoProviderManager userInfoProviderManager;
    private final CustomTokenProvider customTokenProvider;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        logger.info("Request path: {}", path);
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = getTokenFromRequest(request);
            if (token == null) {
                logger.info("No token found in request");
                filterChain.doFilter(request, response);
                return;
            }

            String email = customTokenProvider.getEmailFromToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(request, token, email);
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed: {}", e.getMessage(), e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Authentication failed", e);
        } catch (Exception e) {
            logger.error("Unexpected error in AuthenticationFilter: {}", e.getMessage(), e);
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR,
                    "A problem occurred during the authentication process", e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message, Exception e)
            throws IOException {
        ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(ZoneOffset.UTC),
            status,
            message,
            e.getMessage()
        );
        EndpointResponseDto<ErrorResponseDto> errorResponse = new EndpointResponseDto<>(
            status.value(),
            message,
            error
        );
        
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")
                || path.startsWith("/api/health");
    }

    private void authenticateUser(HttpServletRequest request, String token, String email) {
        if (customTokenProvider.isTokenValid(token, email)) {
            UserDetails userDetails = userInfoProviderManager.getUserDetails(email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("User authenticated successfully");
        } else {
            throw new AuthenticationException("Invalid or expired token");
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