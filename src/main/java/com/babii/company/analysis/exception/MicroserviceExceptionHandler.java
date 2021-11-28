package com.babii.company.analysis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class MicroserviceExceptionHandler {

    @ExceptionHandler(value = {YahooFinanceException.class})
    public ResponseEntity<Object> handle(YahooFinanceException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
