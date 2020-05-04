package com.isd.parking.services;

import com.isd.parking.models.ConfirmationRecord;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public interface ConfirmationService {

    Optional<ConfirmationRecord> findByConfirmationToken(String confirmationToken);

    Optional<ConfirmationRecord> findLastByUsername(String username);

    @NotNull ConfirmationRecord save(ConfirmationRecord confirmationRecord);

    boolean assertNotExpired(ConfirmationRecord confirmationRecord);

    boolean assertValidForRepeat(ConfirmationRecord confirmationRecord);
}
