package com.acmebank.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {
    Logger log = LoggerFactory.getLogger ( GlobalException.class );
    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity badrequest(RuntimeException ex, WebRequest request){
        log.error ( ex.getMessage () );
        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity(exceptionMessage,  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(RuntimeException ex, WebRequest request){
        log.error ( ex.getMessage () );
        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity(exceptionMessage,  HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
