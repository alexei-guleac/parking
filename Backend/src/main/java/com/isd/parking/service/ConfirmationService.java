package com.isd.parking.service;

import com.isd.parking.security.model.ConfirmationRecord;

import java.util.Optional;


public interface ConfirmationService {

    Optional<ConfirmationRecord> findByConfirmationToken(String confirmationToken);

    Optional<ConfirmationRecord> findLastByUsername(String username);

    ConfirmationRecord save(ConfirmationRecord confirmationRecord);

    boolean assertNotExpired(ConfirmationRecord confirmationRecord);

    boolean assertValidForRepeat(ConfirmationRecord confirmationRecord);
}
