package com.isd.parking.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;


/**
 * Exception handler for all controllers.
 * Handles internal errors for non existing pages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Method handles internal errors for non existing pages.
     *
     * @param ex      - ResourceNotFoundException, indicates a resource was not found.
     * @param request - HTTP request provided
     * @return - HTTP response with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public @NotNull ResponseEntity<?> resourceNotFoundException(
        @NotNull ResourceNotFoundException ex, @NotNull WebRequest request) {
        @NotNull ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Method handles common internal errors.
     *
     * @param ex      - exception
     * @param request - HTTP request provided
     * @return - HTTP response with error details
     */
    @ExceptionHandler(Exception.class)
    public @NotNull ResponseEntity<?> globalExceptionHandler(@NotNull Exception ex, @NotNull WebRequest request) {
        @NotNull ErrorResponse errorDetails =
            new ErrorResponse(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

