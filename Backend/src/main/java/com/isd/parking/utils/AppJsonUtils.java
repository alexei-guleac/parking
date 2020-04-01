package com.isd.parking.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AppJsonUtils {

    public static String getJsonStringFromObject(Object object) throws JsonProcessingException {
        // Java objects to JSON string - compact-print
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
