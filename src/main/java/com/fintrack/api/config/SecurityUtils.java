package com.fintrack.api.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated ");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        } 
        if (principal instanceof Integer) { 
            return ((Integer) principal).longValue();
        }

        throw new RuntimeException("User is not authenticated");
    }
}
