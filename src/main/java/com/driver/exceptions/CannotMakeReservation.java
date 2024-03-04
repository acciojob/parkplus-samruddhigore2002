package com.driver.exceptions;

public class CannotMakeReservation extends RuntimeException{

    public CannotMakeReservation(String message){
        super(message);
    }
}
