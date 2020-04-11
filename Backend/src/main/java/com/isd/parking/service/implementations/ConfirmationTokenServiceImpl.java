package com.isd.parking.service.implementations;

import com.isd.parking.repository.ConfirmationTokenRepository;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.service.ConfirmationTokenService;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.isd.parking.utils.AppDateUtils.isDateAfterNow;
import static com.isd.parking.utils.AppDateUtils.isDateBeforeNow;


@Service
@Slf4j
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ColorConsoleOutput console;

    private final int maxClockSkewMinutes = 1;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository, ColorConsoleOutput console) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.console = console;
    }

    /**
     * Get parking lot by id from database method
     *
     * @return - specified parking lot
     */
    @Transactional
    @Override
    public Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken) {
        log.info(console.classMsg(getClass().getSimpleName(), "get confirmationToken..."));
        return Optional.ofNullable(confirmationTokenRepository.findByConfirmationToken(confirmationToken));
    }

    @Transactional
    @Override
    public Optional<ConfirmationToken> findLastByUsername(String username) {
        log.info(console.classMsg(getClass().getSimpleName(), "get confirmationToken..."));
        return Optional.ofNullable(confirmationTokenRepository.findFirstByUidOrderByCreatedAtDesc(username));
    }

    @Transactional
    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public boolean assertNotExpired(ConfirmationToken confirmationToken) {
        return isDateAfterNow(confirmationToken.getExpirationDate(), maxClockSkewMinutes);
    }

    @Override
    public boolean assertValidForRepeat(ConfirmationToken confirmationToken) {
        log.info("expires " + confirmationToken.getCreatedAt().plusDays(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS));
        return isDateBeforeNow(confirmationToken.getCreatedAt().plusDays(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS),
            maxClockSkewMinutes);
    }
}
