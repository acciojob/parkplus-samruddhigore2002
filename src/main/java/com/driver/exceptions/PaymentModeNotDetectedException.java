package com.driver.exceptions;

public class PaymentModeNotDetectedException extends RuntimeException{
    public PaymentModeNotDetectedException(String message){
        super(message);
    }
}
