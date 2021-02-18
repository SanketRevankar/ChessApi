package com.sanket.chess.auth;

import com.sanket.chess.mongodb.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {
    public boolean checkUserHasAccessToUserId(Authentication authentication, String userId) {
        return ((User) authentication.getPrincipal()).getId().equals(userId);
    }
}
