package com.isd.parking.security.exceptions;


public class MalformedJwtException extends RuntimeException {

    public MalformedJwtException(String message) {
        super(message);
    }
}
