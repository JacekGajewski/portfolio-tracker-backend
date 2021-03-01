package com.tracker.portfolio.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
class WebSecurity {
    public boolean checkUserId(Authentication authentication, long id) {
        return authentication.getCredentials().equals(id);
    }
}
