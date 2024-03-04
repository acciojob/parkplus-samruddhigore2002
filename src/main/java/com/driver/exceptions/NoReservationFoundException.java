package com.driver.exceptions;

public class NoReservationFoundException extends RuntimeException{

    public NoReservationFoundException(String message){
        super(message);
    }
}
