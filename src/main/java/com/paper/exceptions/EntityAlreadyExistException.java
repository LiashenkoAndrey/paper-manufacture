package com.paper.exceptions;


public class EntityAlreadyExistException extends RuntimeException{

    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
