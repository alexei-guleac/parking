package com.isd.parking.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * JSON processing utilities
 */
public class AppJsonUtils {

    /**
     * Converts JSON to String
     *
     * @param object - JSON object for parsing
     * @return string representation of JSON object
     * @throws JsonProcessingException
     */
    public static String getJsonStringFromObject(Object object) throws JsonProcessingException {
        // Java objects to JSON string - compact-print
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
