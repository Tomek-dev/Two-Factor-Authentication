package com.security.twofactorsecurity.enums;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum Role implements GrantedAuthority {
    USER("ROLE_USER"),
    PRE_VERIFICATION("ROLE_PRE_VERIFICATION");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
