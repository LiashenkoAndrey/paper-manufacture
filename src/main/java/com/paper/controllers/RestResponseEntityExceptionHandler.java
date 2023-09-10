package com.paper.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

//@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ResponseEntityExceptionHandler.class);

//    @ExceptionHandler(IllegalArgumentException.class)
//    protected ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "IllegalArgument";
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }


//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<?> iOException(RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "IOException";
//        ex.printStackTrace();
//        logger.error(ex);
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }
}
