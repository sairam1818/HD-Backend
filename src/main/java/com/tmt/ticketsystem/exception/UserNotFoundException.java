package com.tmt.ticketsystem.exception;

public class UserNotFoundException extends RuntimeException{
    public  UserNotFoundException(String message)
    {
         super( message);
    }
}
