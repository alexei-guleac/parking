package com.isd.parking.security.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "confirmation_tokens")
@Data
@RequiredArgsConstructor
public class ConfirmationToken {

    private static final int EXPIRATION_IN_DAYS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenId;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false, name = "uid")
    private String uid;

    public ConfirmationToken(String uid) {
        this.uid = uid;
        createdAt = LocalDateTime.now();
        expirationDate = createdAt.plusDays(EXPIRATION_IN_DAYS);
        confirmationToken = UUID.randomUUID().toString();
    }
}
