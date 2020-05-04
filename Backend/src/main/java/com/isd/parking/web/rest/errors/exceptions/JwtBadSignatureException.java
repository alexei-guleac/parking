package com.isd.parking.web.rest.errors.exceptions;


public class JwtBadSignatureException extends RuntimeException {

    public JwtBadSignatureException(String message) {
        super(message);
    }
}
