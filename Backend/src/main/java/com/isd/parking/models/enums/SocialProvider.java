package com.isd.parking.models.enums;

import java.util.stream.Stream;


/**
 * Social service providers
 */
public enum SocialProvider {

    FACEBOOK("fb"),
    GOOGLE("g"),
    MICROSOFT("m"),
    GITHUB("git"),
    VKONTAKTE("vk"),
    AMAZON("a");

    private final String socialProvider;

    SocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public static Stream<SocialProvider> stream() {
        return Stream.of(SocialProvider.values());
    }

    public String getSocialProvider() {
        return socialProvider;
    }

}
