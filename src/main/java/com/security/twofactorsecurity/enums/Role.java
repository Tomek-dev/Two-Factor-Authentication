package com.security.twofactorsecurity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("ROLE_USER"),
    PRE_VERIFICATION("ROLE_PRE_VERIFICATION"),
    ADMIN("ROLE_ADMIN"),
    ANONYMOUS("ROLE_ANONYMOUS");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @Override
    public String getAuthority() {
        return value;
    }
}
