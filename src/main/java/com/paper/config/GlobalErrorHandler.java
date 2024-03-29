package com.paper.config;

import com.paper.exceptions.EntityAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorMessage handleNotFound(final HttpServletRequest request, final Exception error) {
        return ErrorMessage.from("Not Found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityAlreadyExistException.class)
    public ErrorMessage handleEntityAlreadyExist(final HttpServletRequest request, final Exception error) {
        return ErrorMessage.from("Entity with the same data already exist.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMessage constraintViolationException(final HttpServletRequest request, final Exception exception) {
        List<String> violationMessages = new ArrayList<>();
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintEx = ((ConstraintViolationException) exception);
            Set<ConstraintViolation<?>> violations = constraintEx.getConstraintViolations();
            violationMessages = violations.stream()
                    .map((e) -> e.getPropertyPath()  + " - "+ e.getMessage())
                    .collect(Collectors.toList());
        }
        log.info(violationMessages);
        return ErrorMessage.from(violationMessages.toString());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handleAccessDenied(final HttpServletRequest request, final Exception error) {
        return ErrorMessage.from("Permission denied");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorMessage handleInternalError(final HttpServletRequest request, final Exception error) {
        log.error(error);
        if (error instanceof ClientAbortException) {
            log.error(error.getMessage());
            return new ErrorMessage("ClientAbortException");
        }
        throw new RuntimeException(error);
    }
}
