package com.paper.exceptions;

public class ClientMessageNotFoundException extends EntityNotFoundException {

    public ClientMessageNotFoundException() {
    }

    public ClientMessageNotFoundException(String message) {
        super(message);
    }

    public ClientMessageNotFoundException(Throwable cause) {
        super(cause);
    }
}
