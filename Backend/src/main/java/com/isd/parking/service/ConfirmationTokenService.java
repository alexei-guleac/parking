package com.isd.parking.service;

import com.isd.parking.repository.ConfirmationTokenRepository;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.isd.parking.utils.AppDateUtils.isAfterNow;
import static com.isd.parking.utils.AppDateUtils.isBeforeNow;

@Service
@Slf4j
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ColorConsoleOutput console;

    private final int maxClockSkewMinutes = 1;

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
        return isAfterNow(confirmationToken.getExpirationDate(), maxClockSkewMinutes);
    }

    public boolean assertValidForRepeat(ConfirmationToken confirmationToken) {
        log.info("expires " + confirmationToken.getCreatedAt().plusDays(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS));
        return isBeforeNow(confirmationToken.getCreatedAt().plusDays(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS),
            maxClockSkewMinutes);
    }
}
