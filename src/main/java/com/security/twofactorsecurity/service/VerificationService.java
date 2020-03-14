package com.security.twofactorsecurity.service;

import com.security.twofactorsecurity.exceptions.SecretKeyAlreadyExistException;
import com.security.twofactorsecurity.model.User;
import org.springframework.security.core.Authentication;

public interface VerificationService {
    boolean verify(Authentication authentication, String phoneCode);
    void allowVerification(Authentication authentication);
    String generateKey(String username) throws SecretKeyAlreadyExistException;
}
