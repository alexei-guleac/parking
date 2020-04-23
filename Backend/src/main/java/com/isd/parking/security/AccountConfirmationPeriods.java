package com.isd.parking.security;


/**
 * User account confirmation processing (registration, password reset) specified calendar periods
 */
public final class AccountConfirmationPeriods {

    // email confirmation token validity
    public static final int CONFIRM_TOKEN_EXP_IN_MINUTES = 60;

    // allowed repeat registration or forgot password request
    public static final int REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS = 1;

    // allowance for user account operations guarantee
    public static final int MAX_CLOCK_SKEW_MINUTES = 1;

    // allowed repeat reset password request
    public static final int RESET_PASSWORD_PERIOD_IN_DAYS = 30;

}
