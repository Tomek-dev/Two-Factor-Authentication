package com.security.twofactorsecurity;

import com.security.twofactorsecurity.builder.UserBuilder;
import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Start {

    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public Start(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        init();
    }

    private void init(){
        User user = UserBuilder.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles(Collections.singleton("ROLE_USER"))
                .build();
        userDao.save(user);
    }
}
