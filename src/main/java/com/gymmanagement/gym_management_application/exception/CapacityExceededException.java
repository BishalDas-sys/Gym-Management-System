package com.gymmanagement.gym_management_application.exception;

public class CapacityExceededException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CapacityExceededException(String message) {
        super(message);
    }
}
