package com.isd.parking.security.exceptions;


public class JwtExpirationException extends RuntimeException {

    public JwtExpirationException(String message) {
        super(message);
    }
}
