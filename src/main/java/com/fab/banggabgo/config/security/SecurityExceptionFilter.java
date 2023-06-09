package com.fab.banggabgo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (OAuth2AuthorizationException | AuthenticationException | JwtException e) {
            setErrorResponse(response, e.getMessage(), 403);
        } catch (Exception e) {
            setErrorResponse(response, e.getMessage(), 500);
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, int status)
        throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.getWriter().write(message);
    }
}
