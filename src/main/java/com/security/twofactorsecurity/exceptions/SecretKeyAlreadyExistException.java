package com.security.twofactorsecurity.exceptions;

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
