package com.isd.parking.web.rest.errors.exceptions;


public class JwtExpirationException extends RuntimeException {

    public JwtExpirationException(String message) {
        super(message);
    }
}
