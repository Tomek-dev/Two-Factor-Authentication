package com.security.twofactorsecurity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Key")
public class SecretKeyNotFoundException extends RuntimeException{

    public SecretKeyNotFoundException() {
        super("Secret key not found");
    }

    public SecretKeyNotFoundException(String message) {
        super(message);
    }

    public SecretKeyNotFoundException(Throwable cause) {
        super(cause);
    }
}
