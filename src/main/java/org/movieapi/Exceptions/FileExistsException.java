package org.movieapi.Exceptions;

public class FileExistsException extends RuntimeException{

    public FileExistsException(String message){
        super(message);
    }

}
