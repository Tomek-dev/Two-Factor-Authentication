package com.security.twofactorsecurity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Key already exist")
public class SecretKeyAlreadyExistException extends RuntimeException{
    public SecretKeyAlreadyExistException() {
        super("Secret key already exist");
    }

    public SecretKeyAlreadyExistException(String message) {
        super(message);
    }

    public SecretKeyAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
