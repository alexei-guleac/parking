package com.isd.parking.web.rest.payload;

import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.utilities.AppDateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * Different responses with established HTTP headers and localized message bodies
 */
@Service
public class ResponseEntityFactory {

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ResponseEntityFactory(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Creates response entity with Http Status 500
     * @param body - response body object
     * @return response entity with Http Status 500
     */
    public ResponseEntity<?> getServerErrorEntity(Object body) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(body);
    }

    /**
     * Creates response entity with Http Status 404
     * @param body - response body object
     * @return response entity with Http Status 404
     */
    public ResponseEntity<?> getNotFoundEntity(Object body) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(body);
    }

    /*
     ==============================================================================================
        Methods below represents different controllers response entities with localized messages
     ==============================================================================================
     */
    public ResponseEntity<?> confirmationTokenUsed(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-used", null, locale)
                + " " + messageSource.getMessage("AccountController.log-again", null, locale));

    }

    public ResponseEntity<?> confirmationTokenExpired(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-expired", null, locale)
                + " " + messageSource.getMessage("AccountController.reg-again", null, locale));
    }

    public ResponseEntity<?> confirmationTokenNotFound(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-not-found", null, locale)
                + " " + messageSource.getMessage("AccountController.reg-again", null, locale));
    }

    public ResponseEntity<?> passResetNotAllowed(Locale locale) {
        @NotNull String periodEnding = AppDateUtils.getPeriodEndingLocalized(
            AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS, locale);

        return getServerErrorEntity(
            messageSource.getMessage("AccountController.password-reset-allowed", null, locale)
                + " " + periodEnding);
    }

    public ResponseEntity<?> emailNotFound(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.email-notfound", null, locale));
    }

    public ResponseEntity<?> passChangeNotAllowed(Locale locale) {
        @NotNull String periodEnding = AppDateUtils.getPeriodEndingLocalized(
            AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS, locale);

        return getServerErrorEntity(
            messageSource.getMessage("AccountController.password-change-allowed", null, locale)
                + " " + periodEnding);
    }

    public ResponseEntity<?> userNotFound(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.user-notfound", null, locale));
    }

    public ResponseEntity<?> confirmationTokenUserEmpty(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-user-empty", null, locale));
    }

    public ResponseEntity<?> confirmationTokenExpiredRequest(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-expired", null, locale)
                + " " + messageSource.getMessage("AccountController.request-again", null, locale));
    }

    public ResponseEntity<?> confirmationTokenNotFoundRequest(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AccountController.confirmation-token-not-found", null, locale)
                + " " + messageSource.getMessage("AccountController.request-again", null, locale));
    }

    public ResponseEntity<?> socialNotExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AuthenticationController.social-not-exists", null, locale));
    }

    public ResponseEntity<?> userNotExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("AuthenticationController.user-not-exists", null, locale));
    }

    public ResponseEntity<?> userNotCreated(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.user-not-created", null, locale));
    }

    public ResponseEntity<?> socialExistsWaiting(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.social-exists-waiting", null, locale));
    }

    public ResponseEntity<?> socialEmailExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.social-email-exists", null, locale));
    }

    public ResponseEntity<?> socialIdExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.social-id-exists", null, locale));
    }

    public ResponseEntity<?> emailExistsWaiting(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.email-exists-waiting", null, locale));
    }

    public ResponseEntity<?> emailExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.email-exists", null, locale));
    }

    public ResponseEntity<?> usernameExists(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("RegistrationController.username-exists", null, locale));
    }

    public ResponseEntity<?> codeNotProvided(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("SocialController.code-not-provided", null, locale));
    }

    public ResponseEntity<?> socialConnectionError(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("SocialController.connect-err", null, locale));
    }

    public ResponseEntity<?> socialConnectionConflict(Locale locale) {
        return getServerErrorEntity(
            messageSource.getMessage("SocialController.connect-conflict", null, locale));
    }
}
