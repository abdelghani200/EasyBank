package org.example.Exception;

public class AgenceException extends Exception{

    public AgenceException(String message){
        super(message);
    }

    public AgenceException(String message, Throwable cause){
        super(message, cause);
    }

}
