package com.isd.parking.repository;

import com.isd.parking.security.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    ConfirmationToken findFirstByUidOrderByCreatedAtDesc(String username);
}
