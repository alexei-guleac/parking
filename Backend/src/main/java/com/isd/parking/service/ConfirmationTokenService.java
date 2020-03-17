package com.isd.parking.service;

import com.isd.parking.repository.ConfirmationTokenRepository;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ColorConsoleOutput console;

    private final int maxClockSkewMinutes = 1;
    private final int resetPasswordPeriodInDays = 30;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, ColorConsoleOutput console) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.console = console;
    }

    /**
     * Get parking lot by id from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    public Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken) {
        log.info(console.classMsg(getClass().getSimpleName(), "get confirmationToken..."));
        return Optional.ofNullable(confirmationTokenRepository.findByConfirmationToken(confirmationToken));
    }

    @Transactional
    public Optional<ConfirmationToken> findLastByUsername(String username) {
        log.info(console.classMsg(getClass().getSimpleName(), "get confirmationToken..."));
        return Optional.ofNullable(confirmationTokenRepository.findFirstByUidOrderByCreatedAtDesc(username));
    }

    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    public boolean assertNotExpired(ConfirmationToken confirmationToken) {
        return confirmationToken.getExpirationDate().minusMinutes(maxClockSkewMinutes)
                .isAfter(LocalDateTime.now());
        // return DateUtils.isAfter(confirmationToken.getCreatedAt(), LocalDateTime.now(), 60);
    }

    public boolean assertValidForRepeat(ConfirmationToken confirmationToken) {
        log.info("expires " + confirmationToken.getCreatedAt().plusDays(resetPasswordPeriodInDays - 1));
        return confirmationToken.getCreatedAt().plusDays(resetPasswordPeriodInDays - 1).plusMinutes(maxClockSkewMinutes)
                .isBefore(LocalDateTime.now());
    }
}
