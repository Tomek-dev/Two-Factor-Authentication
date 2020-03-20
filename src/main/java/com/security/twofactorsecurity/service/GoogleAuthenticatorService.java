package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.dao.SecretKeyDao;
import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.enums.Role;
import com.security.twofactorsecurity.exceptions.SecretKeyAlreadyExistException;
import com.security.twofactorsecurity.exceptions.SecretKeyNotFoundException;
import com.security.twofactorsecurity.model.SecretKey;
import com.security.twofactorsecurity.model.User;
import com.security.twofactorsecurity.other.TOTPCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private SecretKeyDao secretKeyDao;
    private UserDao userDao;

    @Autowired
    public GoogleAuthenticatorService(SecretKeyDao secretKeyDao, UserDao userDao) {
        this.secretKeyDao = secretKeyDao;
        this.userDao = userDao;
    }

    @Override
    public boolean verify(Authentication authentication, String code) {
        Optional<User> userOptional = userDao.findByUsername(authentication.getName());
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<SecretKey> secretKeyOptional = secretKeyDao.findByUser(user);
        SecretKey secret = secretKeyOptional.orElseThrow(SecretKeyNotFoundException::new);
        String secretKey = TOTPCode.getTOTPCode(secret.getCode());
        if(code.equals(secretKey)){
            Authentication verified = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
            SecurityContextHolder.getContext().setAuthentication(verified);
            return true;
        }
        return false;
    }

    @Override
    public String allowVerification(Authentication authentication) {
        long currentTime = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", Role.PRE_VERIFICATION.getAuthority())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 300000))
                .signWith(SignatureAlgorithm.HS256, "!oRE=#QDF13/&Ym".getBytes())
                .compact();
        return "Bearer " + token;
    }

    @Override
    public String generateKey(String username) throws SecretKeyAlreadyExistException {
        Optional<User> userOptional = userDao.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<SecretKey> secretKeyOptional = secretKeyDao.findByUser(user);
        if(secretKeyOptional.isPresent()){
            throw new SecretKeyAlreadyExistException();
        }
        String secret = TOTPCode.generateKey();
        SecretKey secretKey = new SecretKey(secret, user); //TODO encrypt secretKey in db
        secretKeyDao.save(secretKey);
        return secret;
    }
}
