package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.builder.UserBuilder;
import com.security.twofactorsecurity.dao.SecretKeyDao;
import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.enums.Role;
import com.security.twofactorsecurity.exceptions.SecretKeyAlreadyExistException;
import com.security.twofactorsecurity.model.SecretKey;
import com.security.twofactorsecurity.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GoogleAuthenticatorServiceTest {

    @Mock
    private SecretKeyDao secretKeyDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private GoogleAuthenticatorService authenticatorService;

    private Authentication authentication;

    @Before
    public void prepare(){
        authentication = new AnonymousAuthenticationToken("key", "anonymousUser", Collections.singleton(new SimpleGrantedAuthority("ANONYMOUS")));
    }

    @Test
    public void shouldThrowUsernameNotFoundException(){
        //given
        given(userDao.findByUsername(Mockito.any())).willReturn(java.util.Optional.empty());

        //then
        assertThrows(UsernameNotFoundException.class, () -> authenticatorService.verify(authentication, "4"));
        assertThrows(UsernameNotFoundException.class, () -> authenticatorService.generateKey("username"));
    }

    @Test(expected = SecretKeyAlreadyExistException.class)
    public void shouldThrowSecretKeyAlreadyExistException(){
        //given
        User user = new User();
        SecretKey secretKey = new SecretKey("key", user);
        given(userDao.findByUsername(Mockito.any())).willReturn(java.util.Optional.of(user));
        given(secretKeyDao.findByUser(Mockito.any())).willReturn(java.util.Optional.of(secretKey));

        //when
        authenticatorService.generateKey("username");
    }

    @Test
    public void shouldGenerateKey(){
        //given
        User user = new User();
        given(userDao.findByUsername(Mockito.any())).willReturn(java.util.Optional.of(user));
        given(secretKeyDao.findByUser(Mockito.any())).willReturn(java.util.Optional.empty());

        //when
        authenticatorService.generateKey("username");

        //then
        verify(secretKeyDao, times(1)).save(any());
    }

    @Test
    public void shouldAllowVerification(){
        //when
        authenticatorService.allowVerification(authentication);

        //then
        assertEquals(authentication.getName(), SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals(Role.PRE_VERIFICATION, SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]);
    }
}