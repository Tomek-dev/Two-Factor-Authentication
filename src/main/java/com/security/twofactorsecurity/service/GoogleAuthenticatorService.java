package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.dao.SecretCodeDao;
import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.enums.Role;
import com.security.twofactorsecurity.exceptions.SecretKeyNotFoundException;
import com.security.twofactorsecurity.model.SecretCode;
import com.security.twofactorsecurity.model.User;
import com.security.twofactorsecurity.other.TOTPCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("GoogleAuthenticatorService")
@Qualifier("GoogleAuthenticatorService")
public class GoogleAuthenticatorService implements VerificationService {

    private SecretCodeDao secretCodeDao;
    private UserDao userDao;

    @Autowired
    public GoogleAuthenticatorService(SecretCodeDao secretCodeDao, UserDao userDao) {
        this.secretCodeDao = secretCodeDao;
        this.userDao = userDao;
    }

    @Override
    public boolean verify(Authentication authentication, String code) {
        Optional<User> userOptional = userDao.findByUsername(authentication.getName());
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<SecretCode> secretCodeOptional = secretCodeDao.findByUser(user);
        SecretCode secretCode = secretCodeOptional.orElseThrow(SecretKeyNotFoundException::new);
        String secretKey = TOTPCode.getTOTPCode(secretCode.getCode());
        if(code.equals(secretKey)){
            Authentication verified = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
            SecurityContextHolder.getContext().setAuthentication(verified);
            return true;
        }
        return false;
    }

    @Override
    public void allowVerification(Authentication authentication) {
        Authentication allowAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getName(), null, Collections.singleton(Role.PRE_VERIFICATION));
        SecurityContextHolder.getContext().setAuthentication(allowAuthentication);
    }

    @Override
    public String generateKey(String username) {
        return null;
    }
}