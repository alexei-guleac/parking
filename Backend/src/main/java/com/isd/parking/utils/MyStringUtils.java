package com.isd.parking.utils;

import java.util.Arrays;
import java.util.Collection;

import static org.apache.commons.lang.StringUtils.strip;

public class MyStringUtils {

    // public String collectionToString(Collection e) {
    //     return Arrays.toString(e.toArray()).replace("[", "").replace("]", "");
    // }

    public String collectionToString(Collection e) {
        return strip(strip(Arrays.toString(e.toArray()), "["), "]");
    }
}
