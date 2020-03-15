package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException(){
        //given
        given(userDao.findByUsername(Mockito.any())).willReturn(Optional.empty());

        //when
        userDetailsService.loadUserByUsername("username");
    }
}