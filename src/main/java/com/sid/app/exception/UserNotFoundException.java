package com.sid.app.exception;

/**
 * @author Siddhant Patni
 */
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2359863924124256L;

    public UserNotFoundException(String id) {
        super("Could not found the user with id " + id);
    }

}