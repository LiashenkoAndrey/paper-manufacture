package com.paper.exceptions;

public class CatalogNotFoundException extends EntityNotFoundException {
    public CatalogNotFoundException() {
    }

    public CatalogNotFoundException(String message) {
        super(message);
    }

    public CatalogNotFoundException(Throwable cause) {
        super(cause);
    }
}
