package com.isd.parking.security.model.payload;

public class GRecaptcha {

    // the secret key from google developer admin console;
    public static final String SECRET_KEY = "6Lcngd8UAAAAAGP04a5nTLgeZKXDWKasd-A1rHHQ";

    // token validation url is URL: https://www.google.com/recaptcha/api/siteverify (Google ReCaptcha v2)
    // METHOD used is: POST
    public static final String GRECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
}
