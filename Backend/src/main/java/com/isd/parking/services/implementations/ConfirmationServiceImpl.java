package com.isd.parking.services.implementations;

import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.repository.ConfirmationTokenRepository;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.services.ConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.isd.parking.security.AccountConfirmationPeriods.MAX_CLOCK_SKEW_MINUTES;
import static com.isd.parking.utilities.AppDateUtils.isDateAfterNow;
import static com.isd.parking.utilities.AppDateUtils.isDateBeforeNow;
import static com.isd.parking.utilities.ColorConsoleOutput.methodMsg;


@Service
@Slf4j
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    /**
     * Gets confirmation record by confirmation token
     *
     * @return correspond confirmation record
     */
    @Transactional
    @Override
    public Optional<ConfirmationRecord> findByConfirmationToken(String confirmationToken) {
        log.info(methodMsg("get confirmationToken..."));
        return Optional.of(confirmationTokenRepository.findByConfirmationToken(confirmationToken));
    }

    /**
     * Gets last operation confirmation record by username
     *
     * @param username - target user name
     * @return last correspond confirmation record
     */
    @Transactional
    @Override
    public Optional<ConfirmationRecord> findLastByUsername(String username) {
        log.info(methodMsg("get confirmationToken..."));
        return Optional.ofNullable(confirmationTokenRepository.findFirstByUidOrderByCreatedAtDesc(username));
    }

    /**
     * Save confirmation record in database
     *
     * @param confirmationRecord - specified confirmation record
     * @return successfully saved confirmation record
     */
    @Transactional
    @Override
    public @NotNull ConfirmationRecord save(@NotNull ConfirmationRecord confirmationRecord) {
        return confirmationTokenRepository.save(confirmationRecord);
    }

    /**
     * Assert confirmation not expired
     *
     * @param confirmationRecord - specified confirmation record
     * @return operation result
     */
    @Override
    public boolean assertNotExpired(@NotNull ConfirmationRecord confirmationRecord) {
        return isDateAfterNow(confirmationRecord.getExpirationDate(), MAX_CLOCK_SKEW_MINUTES);
    }

    /**
     * Make sure enough time have passed from confirmation creation date for allow repeat confirmation request
     *
     * @param confirmationRecord - specified confirmation record
     * @return operation result
     */
    @Override
    public boolean assertValidForRepeat(@NotNull ConfirmationRecord confirmationRecord) {
        return isDateBeforeNow(confirmationRecord.getCreatedAt().plusDays(
            AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS),
            MAX_CLOCK_SKEW_MINUTES);
    }
}
