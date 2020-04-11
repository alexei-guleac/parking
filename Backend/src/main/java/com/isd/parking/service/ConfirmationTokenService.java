package com.isd.parking.service;

import com.isd.parking.security.model.ConfirmationToken;

import java.util.Optional;


public interface ConfirmationTokenService {

    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

    Optional<ConfirmationToken> findLastByUsername(String username);

    ConfirmationToken save(ConfirmationToken confirmationToken);

    boolean assertNotExpired(ConfirmationToken confirmationToken);

    boolean assertValidForRepeat(ConfirmationToken confirmationToken);
}
