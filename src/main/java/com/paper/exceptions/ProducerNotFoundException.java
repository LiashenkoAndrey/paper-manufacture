package com.paper.exceptions;

public class ProducerNotFoundException extends EntityNotFoundException {

    public ProducerNotFoundException() {
    }

    public ProducerNotFoundException(String message) {
        super(message);
    }

    public ProducerNotFoundException(Throwable cause) {
        super(cause);
    }
}
