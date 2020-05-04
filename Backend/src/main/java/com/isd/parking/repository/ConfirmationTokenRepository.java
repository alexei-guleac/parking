package com.isd.parking.repository;

import com.isd.parking.models.ConfirmationRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * User account confirmation token database repository
 */
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationRecord, String> {

    @NotNull ConfirmationRecord findByConfirmationToken(String confirmationToken);

    @NotNull ConfirmationRecord findFirstByUidOrderByCreatedAtDesc(String username);
}
