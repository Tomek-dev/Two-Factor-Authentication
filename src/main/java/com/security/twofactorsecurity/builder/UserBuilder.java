package com.security.twofactorsecurity.builder;

import com.security.twofactorsecurity.enums.Role;
import com.security.twofactorsecurity.model.User;

import java.util.Set;

public class UserBuilder {

    private User user = new User();

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public UserBuilder username(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder password(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder roles(Set<Role> roles){
        user.setRoles(roles);
        return this;
    }

    public UserBuilder using2FA(Boolean using2FA){
        user.setUsing2FA(using2FA);
        return this;
    }

    public User build(){
        return user;
    }
}
