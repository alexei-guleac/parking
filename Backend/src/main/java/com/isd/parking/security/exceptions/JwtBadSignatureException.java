package com.isd.parking.security.exceptions;


public class JwtBadSignatureException extends RuntimeException {
    public JwtBadSignatureException(String message) {
        super(message);
    }
}
