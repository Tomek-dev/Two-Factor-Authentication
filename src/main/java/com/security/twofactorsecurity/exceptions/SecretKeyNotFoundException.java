package com.security.twofactorsecurity.exceptions;

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
