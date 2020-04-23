package com.isd.parking.repository;

import com.isd.parking.security.model.ConfirmationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * User account confirmation token database repository
 */
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationRecord, String> {

    ConfirmationRecord findByConfirmationToken(String confirmationToken);

    ConfirmationRecord findFirstByUidOrderByCreatedAtDesc(String username);
}
