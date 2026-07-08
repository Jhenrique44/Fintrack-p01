package com.fintrack.api.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fintrack.api.exception.UnauthorizedException;

public class SecurityUtils {

    public static Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Token is not valid or expired");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        } 
        if (principal instanceof Integer) { 
            return ((Integer) principal).longValue();
        }

        throw new UnauthorizedException("Token is not valid or expired");
    }
}
