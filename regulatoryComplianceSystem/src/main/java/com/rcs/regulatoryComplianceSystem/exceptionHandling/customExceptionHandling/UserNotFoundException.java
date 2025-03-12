package com.rcs.regulatoryComplianceSystem.exceptionHandling.customExceptionHandling;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
