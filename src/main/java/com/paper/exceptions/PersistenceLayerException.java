package com.paper.exceptions;

public class PersistenceLayerException extends RuntimeException {

    public PersistenceLayerException() {
    }

    public PersistenceLayerException(String message) {
        super(message);
    }


    public PersistenceLayerException(Throwable cause) {
        super(cause);
    }
}
