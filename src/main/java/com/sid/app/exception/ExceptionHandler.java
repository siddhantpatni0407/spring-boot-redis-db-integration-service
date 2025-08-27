package com.sid.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Handles exceptions thrown by controllers and returns appropriate HTTP responses.
 * <p>
 * Currently handles UserNotFoundException and returns a NOT_FOUND status with an error message.
 *
 * @author Siddhant Patni
 */
@ControllerAdvice
public class ExceptionHandler {

    /**
     * Handles UserNotFoundException thrown by any controller.
     * Returns a map containing the error message and sets the HTTP status to NOT_FOUND (404).
     *
     * @param exception the UserNotFoundException instance
     * @return a map with the error message
     */
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> exceptionHandler(UserNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", exception.getMessage());
        return errorMap;
    }

}
