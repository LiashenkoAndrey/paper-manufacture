package com.paper.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManufactureMachineNotFoundException extends EntityNotFoundException {

    public ManufactureMachineNotFoundException() {
    }

    public ManufactureMachineNotFoundException(Exception cause) {
        super(cause);
    }

    public ManufactureMachineNotFoundException(String message) {
        super(message);
    }
}
