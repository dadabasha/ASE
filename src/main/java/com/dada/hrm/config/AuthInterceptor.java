package com.dada.hrm.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getSession(false) != null && request.getSession(false).getAttribute("userId") != null) {
            return true;
        }
        if (request.getRequestURI().startsWith("/api/")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Login required");
        } else {
            response.sendRedirect("/login");
        }
        return false;
    }
}
