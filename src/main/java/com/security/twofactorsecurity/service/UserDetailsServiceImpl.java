package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.builder.UserBuilder;
import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.findByUsername(s);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserBuilder.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
