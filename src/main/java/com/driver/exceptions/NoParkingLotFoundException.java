package com.driver.exceptions;

public class NoParkingLotFoundException extends RuntimeException{

    public NoParkingLotFoundException(String message){
        super(message);
    }
}
