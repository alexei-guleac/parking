package com.isd.parking.web.rest.errors.exceptions;


public class MalformedJwtException extends RuntimeException {

    public MalformedJwtException(String message) {
        super(message);
    }
}
