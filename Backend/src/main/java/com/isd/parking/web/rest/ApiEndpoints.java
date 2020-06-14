package com.isd.parking.web.rest;

import org.springframework.stereotype.Component;


/**
 * Backend application API endpoints
 */
@Component
public class ApiEndpoints {

    public static final String users = "/users";

    public static final String parking = "/parking";

    public static final String statistics = "/statistics";

    public static final String statisticsByLot = "/lot_statistics";

    public static final String arduinoApi = "/arduino";

    public static final String arduinoWS = "/demo";

    public static final String profile = "/profile";

    public static final String profileUpdate = "/profile" + "/update";

    public static final String profileDelete = "/profile" + "/delete";

    public static final String auth = "/auth";

    public static final String login = "/login";

    public static final String register = "/register";

    public static final String gitOAuth = "/github_oauth";

    public static final String socialLogin = "/social";

    public static final String socialConnect = "/social_connect";

    public static final String socialDisconnect = "/social_disconnect";

    public static final String resetPassword = "/reset_password";

    public static final String forgotPassword = "/forgot_password";

    public static final String confirmAction = "/confirm_account";

    public static final String confirmReset = "/confirm_reset";

    public static final String validateCaptcha = "/validate_captcha";

    public static final String reserve = "/reserve";

    public static final String unreserve = "/unreserve";

    public static final String frontWS = "/front-ws";

    public static final String frontWSTopic = "/arduino/lot-messages";

}
