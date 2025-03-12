package com.rcs.regulatoryComplianceSystem.exceptionHandling.customExceptionHandling;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }

}
