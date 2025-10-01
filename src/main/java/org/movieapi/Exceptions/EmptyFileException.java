package org.movieapi.Exceptions;

public class EmptyFileException extends RuntimeException{

    public EmptyFileException(String message){
        super(message);
    }
}
